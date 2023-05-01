package com.machina.world;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.machina.Machina;
import com.machina.planet.attribute.AttributeList;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.registration.init.AttributeInit;
import com.machina.util.helper.PlanetHelper;
import com.machina.util.helper.ServerHelper;
import com.machina.util.math.OpenSimplex2F;
import com.machina.world.cave.PlanetCarver;
import com.machina.world.data.StarchartData;
import com.machina.world.feature.planet.PlanetOreFeature;
import com.machina.world.feature.planet.tree.PlanetTreeFeature;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetBlocksGenerator.BlockPalette;
import com.machina.world.gen.PlanetNoiseGenerator;
import com.machina.world.gen.PlanetSlopeGenerator;
import com.machina.world.gen.PlanetTerrainGenerator;
import com.machina.world.gen.PlanetTerrainGenerator.IPlanetTerrainProcessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.Dimension;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

public class PlanetChunkGenerator extends ChunkGenerator {

	//@formatter:off
	public static final Codec<PlanetChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.create(Registry.BIOME_REGISTRY).fieldOf("biomes").forGetter(PlanetChunkGenerator::getBiomeRegistry),
			Codec.INT.fieldOf("id").forGetter(PlanetChunkGenerator::getId)
		).apply(instance, PlanetChunkGenerator::new));
	//@formatter:on

	// Blockstates
	public static final BlockState AIR = Blocks.AIR.defaultBlockState();
	public static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();

	// Chunk Settings
	private final int chunkWidth = 16;
	private final int chunkHeight = 16;

	// Random Settings
	public SharedSeedRandom random;
	private OpenSimplex2F caveDecoNoise;
	private PlanetNoiseGenerator noiseGenerator;

	// Noise Settings
	public IPlanetTerrainProcessor surfproc;
	public double surfscale = 1D;
	public double surfdetail = 1D;
	public double surfroughness = 0.5D;
	public double surfdistortion = 0D;
	public double surfmodifier = 0.5D;

	// Chunk Generator Properties
	private final Registry<Biome> biomes;
	private final Biome biome;
	private int id;
	private long seed;
	private List<? extends IWorldTrait> traits;
	List<Supplier<ConfiguredCarver<?>>> carvers = new ArrayList<>();
	List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>();

	// Chunk generator config
	public boolean cavesExist = false;
	public float caveChance = 0f;
	public int caveLength = 0;
	public float caveThickness = 0f;
	public BlockPalette baseBlocks;
	public BlockPalette surfBlocks;
	public BlockPalette fluidBlocks;

	public Registry<Biome> getBiomeRegistry() {
		return biomes;
	}

	public PlanetChunkGenerator(MinecraftServer server, RegistryKey<Dimension> key) {
		this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), PlanetHelper.getIdDim(key));

		// Traits
		this.traits = StarchartData.getDefaultInstance(server).getTraitsForType(key.location(), IWorldTrait.class);
		this.traits.forEach(t -> t.init(this));

		// Attributes
		AttributeList attr = StarchartData.getStarchartForServer(server).get(key.location()).getAttributes();
		this.cavesExist = attr.getValue(AttributeInit.CAVES_EXIST) == 1;
		this.caveChance = attr.getValue(AttributeInit.CAVE_CHANCE);
		this.caveLength = attr.getValue(AttributeInit.CAVE_LENGTH);
		this.caveThickness = attr.getValue(AttributeInit.CAVE_THICKNESS);
		this.baseBlocks = PlanetBlocksGenerator.getBasePalette(attr.getValue(AttributeInit.BASE_BLOCKS));
		this.surfBlocks = PlanetBlocksGenerator.getSurfPalette(attr.getValue(AttributeInit.SURF_BLOCKS));
		this.fluidBlocks = PlanetBlocksGenerator.getFluidPalette(attr.getValue(AttributeInit.FLUID_BLOCKS));

		// Post Init
		this.carvers = new ArrayList<>();
		if (this.cavesExist)
			this.carvers.add(PlanetCarver.create(this.caveChance, this.caveLength, this.caveThickness));
		this.traits.forEach(trait -> this.carvers.addAll(trait.addCarvers(this)));

		this.features = new ArrayList<>();
		this.features.add(() -> new PlanetTreeFeature(attr).countchance(attr.getValue(AttributeInit.TREE_COUNT),
				attr.getValue(AttributeInit.TREE_CHANCE)));
		this.features.add(() -> new PlanetOreFeature(attr).count(1000));
		this.traits.forEach(trait -> this.features.addAll(trait.addFeatures(this)));

		// Noise
		this.surfproc = PlanetTerrainGenerator.getProcessor(attr.getValue(AttributeInit.SURFACE_SHAPE));
		this.surfscale = attr.getValue(AttributeInit.SURFACE_SCALE);
		this.surfdetail = attr.getValue(AttributeInit.SURFACE_DETAIL);
		this.surfroughness = attr.getValue(AttributeInit.SURFACE_ROUGHNESS);
		this.surfdistortion = attr.getValue(AttributeInit.SURFACE_DISTORTION);
		this.surfmodifier = attr.getValue(AttributeInit.SURFACE_MODIFIER);

	}

	public PlanetChunkGenerator(Registry<Biome> biomes, int id) {
		super(new SingleBiomeProvider(
				biomes.getOrThrow(RegistryKey.create(Registry.BIOME_REGISTRY, Machina.MACHINA_ID))),
				new PlanetStructureSettings());

		// Settings
		this.id = id;
		this.seed = ServerHelper.getSeed() + this.id;
		this.biomes = biomes;
		this.biome = biomeSource.getNoiseBiome(0, 0, 0);
		this.random = new SharedSeedRandom(this.seed);

		// NOISEE
		this.caveDecoNoise = new OpenSimplex2F(this.seed);
		this.noiseGenerator = new PlanetNoiseGenerator(this.seed, this);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		this.seed = seed;
		this.random = new SharedSeedRandom(this.seed);
		this.caveDecoNoise = new OpenSimplex2F(this.seed);
		this.noiseGenerator = new PlanetNoiseGenerator(this.seed, this);
		return this;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk chunk) {

		List<BlockPos> pos = new ArrayList<>();
		ChunkPos chunkpos = chunk.getPos();
		this.random.setBaseChunkSeed(chunkpos.x, chunkpos.z);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		for (int i1 = 0; i1 < 16; ++i1) {
			for (int j1 = 0; j1 < 16; ++j1) {
				int k1 = chunkpos.getMinBlockX() + i1;
				int l1 = chunkpos.getMinBlockZ() + j1;

				IBlockReader column = getBaseColumn(k1, l1);
				for (int y = 0; y < getGenDepth(); y++) {
					blockpos$mutable.set(k1, y, l1);
					BlockState state = column.getBlockState(blockpos$mutable);
					chunk.setBlockState(blockpos$mutable, state, false);
					if (!state.isAir()) {
						blockpos$mutable.move(0, 1, 0);
						if (column.getBlockState(blockpos$mutable).isAir()) {
							pos.add(blockpos$mutable.immutable());
						}
						blockpos$mutable.move(0, -2, 0);
						if (column.getBlockState(blockpos$mutable).isAir()) {
							pos.add(blockpos$mutable.immutable());
						}
					}
				}
			}
		}

		this.placeBedrock(chunk, this.random);
		this.placeCaves(seed, chunk);
		for (BlockPos p : pos) {
			PlanetSlopeGenerator.decorateAt(chunk, p, this, false);
		}
	}

	protected BlockState generateBaseState(double chance) {
		return chance > 0D ? baseBlocks.getBaseBlock() : AIR;
	}

	@Override
	public void applyBiomeDecoration(WorldGenRegion pRegion, StructureManager pStructureManager) {
		this.placeFeatures(pRegion);
	}

	public void placeFeatures(WorldGenRegion region) {

		int i = region.getCenterX();
		int j = region.getCenterZ();
		int k = i * 16;
		int l = j * 16;
		BlockPos blockpos = new BlockPos(k, 0, l);

		try {
			for (Supplier<ConfiguredFeature<?, ?>> supplier : features) {
				ConfiguredFeature<?, ?> configuredfeature = supplier.get();

				try {
					configuredfeature.place(region, this, new Random(this.random.nextLong()), blockpos);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception exception) {
			CrashReport crashreport = CrashReport.forThrowable(exception, "Biome decoration");
			crashreport.addCategory("Generation").setDetail("CenterX", i).setDetail("CenterZ", j);
			throw new ReportedException(crashreport);
		}
	}

	public void placeCaves(long seed, IChunk chunk) {

		Function<BlockPos, Biome> getBiome = (pos) -> biome;
		ChunkPos chunkpos = chunk.getPos();
		int j = chunkpos.x;
		int k = chunkpos.z;
		BitSet bitset = ((ChunkPrimer) chunk).getOrCreateCarvingMask(Carving.AIR);

		for (int l = j - 8; l <= j + 8; ++l) {
			for (int i1 = k - 8; i1 <= k + 8; ++i1) {
				int l1 = 0;
				for (Supplier<ConfiguredCarver<?>> supplier : this.carvers) {
					ConfiguredCarver<?> configuredcarver = supplier.get();
					this.random.setLargeFeatureSeed(seed + (long) l1, l, i1);
					configuredcarver.carve(chunk, getBiome, this.random, this.getSeaLevel(), l, i1, j, k, bitset);
					if (configuredcarver.isStartChunk(this.random, l, i1)) {
						configuredcarver.carve(chunk, getBiome, this.random, this.getSeaLevel(), l, i1, j, k, bitset);
					}
					l1++;
				}
			}
		}

		for (int a = 0; a < bitset.length(); ++a) {
			if (bitset.get(a)) {
				BlockPos pos = new BlockPos(chunkpos.getMinBlockX() + (a & 15), (a >> 8),
						chunkpos.getMinBlockZ() + (a >> 4 & 15));
				PlanetSlopeGenerator.decorateAt(chunk, pos, this, true);
			}
		}

	}

	private void placeBedrock(IChunk chunk, Random random) {

		boolean b = surfproc.hasBotBedrock();
		boolean t = surfproc.hasTopBedrock();
		if (!b && !t)
			return;

		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = chunk.getPos().getMinBlockX();
		int j = chunk.getPos().getMinBlockZ();
		for (BlockPos blockpos : BlockPos.betweenClosed(i, 0, j, i + 15, 0, j + 15)) {
			for (int k1 = 4; k1 >= 0; --k1) {
				if (k1 <= random.nextInt(5)) {
					if (b) {
						chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k1, blockpos.getZ()), BEDROCK, false);
					}

					if (t) {
						int k2 = getGenDepth() - k1 - 1;
						chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k2, blockpos.getZ()), BEDROCK, false);
					}
				}

			}
		}
	}

	@Override
	public IBlockReader getBaseColumn(int x, int z) {
		BlockState[] ablockstate = new BlockState[getGenDepth()];
		this.iterateNoiseColumn(x, z, ablockstate, (Predicate<BlockState>) null);
		return new Blockreader(ablockstate);
	}

	@Override
	public int getBaseHeight(int x, int z, Type heightmapType) {
		return this.iterateNoiseColumn(x, z, (BlockState[]) null, heightmapType.isOpaque());
	}

	private double[] makeAndFillNoiseColumn(int x, int z) {
		double[] adouble = new double[getGenDepth() / this.chunkHeight + 1];
		this.fillNoiseColumn(adouble, x, z);
		return adouble;
	}

	private void fillNoiseColumn(double[] out, int x, int z) {
		for (int i1 = 0; i1 <= getGenDepth() / this.chunkHeight; ++i1) {
			out[i1] = noiseGenerator.at(x, i1, z);
		}
	}

	private int iterateNoiseColumn(int x, int z, @Nullable BlockState[] column, @Nullable Predicate<BlockState> test) {
		int locX = Math.floorDiv(x, this.chunkWidth);
		int locY = Math.floorDiv(z, this.chunkWidth);
		int k = Math.floorMod(x, this.chunkWidth);
		int l = Math.floorMod(z, this.chunkWidth);
		double d0 = (double) k / (double) this.chunkWidth;
		double d1 = (double) l / (double) this.chunkWidth;
		double[][] adouble = new double[][] { this.makeAndFillNoiseColumn(locX, locY),
				this.makeAndFillNoiseColumn(locX, locY + 1), this.makeAndFillNoiseColumn(locX + 1, locY),
				this.makeAndFillNoiseColumn(locX + 1, locY + 1) };

		for (int i1 = getGenDepth() / this.chunkHeight - 1; i1 >= 0; --i1) {
			double d2 = adouble[0][i1];
			double d3 = adouble[1][i1];
			double d4 = adouble[2][i1];
			double d5 = adouble[3][i1];
			double d6 = adouble[0][i1 + 1];
			double d7 = adouble[1][i1 + 1];
			double d8 = adouble[2][i1 + 1];
			double d9 = adouble[3][i1 + 1];

			for (int j1 = this.chunkHeight - 1; j1 >= 0; --j1) {
				double d10 = (double) j1 / (double) this.chunkHeight;
				double d11 = MathHelper.lerp3(d10, d0, d1, d2, d6, d4, d8, d3, d7, d5, d9);
				int y = i1 * this.chunkHeight + j1;
				double chance = surfproc.postprocess(y, d11, getGenDepth(), surfmodifier);
				BlockState blockstate = this.generateBaseState(chance);
				if (column != null) {
					column[y] = blockstate;
				}

				if (test != null && test.test(blockstate)) {
					return y + 1;
				}
			}
		}

		return 0;
	}

	@Override
	public void fillFromNoise(IWorld world, StructureManager structure, IChunk chunk) {
	}

	public int getId() {
		return id;
	}

	public OpenSimplex2F getCaveDecoNoise() {
		return caveDecoNoise;
	}

	public static class PlanetStructureSettings extends DimensionStructuresSettings {

		public PlanetStructureSettings() {
			super(false);
			structureConfig.clear();
		}
	}
}
