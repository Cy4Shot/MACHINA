package com.machina.world;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.machina.Machina;
import com.machina.planet.attribute.PlanetAttributeList;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.registration.init.AttributeInit;
import com.machina.util.math.OpenSimplex2F;
import com.machina.util.server.PlanetHelper;
import com.machina.util.server.ServerHelper;
import com.machina.util.text.MachinaRL;
import com.machina.world.cave.PlanetCarver;
import com.machina.world.cave.PlanetCaveDecorator;
import com.machina.world.data.StarchartData;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetBlocksGenerator.BlockPalette;
import com.machina.world.settings.PlanetNoiseSettings;
import com.machina.world.settings.PlanetStructureSettings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
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
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.BlockStateProvidingFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class PlanetChunkGenerator extends ChunkGenerator {

	//@formatter:off
	public static final Codec<PlanetChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			RegistryLookupCodec.create(Registry.BIOME_REGISTRY).fieldOf("biomes").forGetter(PlanetChunkGenerator::getBiomeRegistry),
			Codec.INT.fieldOf("id").forGetter(PlanetChunkGenerator::getId)
		).apply(instance, PlanetChunkGenerator::new));
	//@formatter:on

	private static final float[] BIOME_WEIGHTS = Util.make(new float[25], (p_236092_0_) -> {
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				p_236092_0_[i + 2 + (j + 2) * 5] = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
			}
		}

	});

	public static final BlockState AIR = Blocks.AIR.defaultBlockState();
	public static final BlockState BEDROCK = Blocks.BEDROCK.defaultBlockState();

	// Chunk Settings
	private final int chunkWidth = 16;
	private final int chunkHeight = 16;

	// Random Settings
	public final SharedSeedRandom random;
//	private final INoiseGenerator surfaceNoise;
	private final OpenSimplex2F caveDecoNoise;
	private final SimplexNoiseGenerator islandNoise;
	private final OctavesNoiseGenerator depthNoise;
	private final OctavesNoiseGenerator minLimitPerlinNoise;
	private final OctavesNoiseGenerator maxLimitPerlinNoise;
	private final OctavesNoiseGenerator mainPerlinNoise;

	// Noise Settings
	public PlanetNoiseSettings noisesettings = PlanetNoiseSettings.OVERWORLD_TYPE;
	private double xzscale = 0D;
	private double yscale = 0D;
	private double xzfactor = 0D;
	private double yfactor = 0D;
	private double topTarget = 0D;
	private double topSize = 0D;
	private double topOffset = 0D;
	private double botTarget = 0D;
	private double botSize = 0D;
	private double botOffset = 0D;
	private boolean randDensityEnabled = false;
	private double randFactor = 0D;
	private double randOffset = 0D;

	// Chunk Generator Properties
	private final Registry<Biome> biomes;
	private final Biome biome;
	private int id;
	private long seed = 0L;
	private List<? extends IWorldTrait> traits;
	List<Supplier<ConfiguredCarver<?>>> carvers = new ArrayList<>();
	List<Supplier<ConfiguredFeature<?, ?>>> features = new ArrayList<>();

	// Chunk generator config
	public int seaLevel = -1;
	public float heightMultiplier = 1f;
	public boolean cavesExist = false;
	public boolean frozen = false;
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
		PlanetAttributeList attr = StarchartData.getStarchartForServer(server).get(key.location()).getAttributes();
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
		this.features.add(() -> Feature.BLOCK_PILE
				.configured(new BlockStateProvidingFeatureConfig(
						new SimpleBlockStateProvider(Blocks.ACACIA_LOG.defaultBlockState())))
				.decorated(Placement.HEIGHTMAP_WORLD_SURFACE.configured(new NoPlacementConfig())));
		this.traits.forEach(trait -> this.features.addAll(trait.addFeatures(this)));

		// Noise
		this.xzscale = 684.412D * this.noisesettings.noiseSamplingSettings().xzScale();
		this.yscale = 684.412D * this.noisesettings.noiseSamplingSettings().yScale() * (1f / this.heightMultiplier);
		this.xzfactor = this.xzscale / this.noisesettings.noiseSamplingSettings().xzFactor();
		this.yfactor = this.yscale / this.noisesettings.noiseSamplingSettings().yFactor();
		this.topTarget = (double) this.noisesettings.topSlideSettings().target();
		this.topSize = (double) this.noisesettings.topSlideSettings().size();
		this.topOffset = (double) this.noisesettings.topSlideSettings().offset();
		this.botSize = (double) this.noisesettings.bottomSlideSettings().size();
		this.botOffset = (double) this.noisesettings.bottomSlideSettings().offset();
		this.randDensityEnabled = this.noisesettings.randomDensityOffset();
		this.randFactor = this.noisesettings.densityFactor();
		this.randOffset = this.noisesettings.densityOffset();

	}

	public PlanetChunkGenerator(Registry<Biome> biomes, int id) {
		super(new SingleBiomeProvider(
				biomes.getOrThrow(RegistryKey.create(Registry.BIOME_REGISTRY, new MachinaRL(Machina.MOD_ID)))),
				new PlanetStructureSettings());

		// Settings
		this.id = id;
		this.seed = ServerHelper.getSeed() + this.id;
		this.biomes = biomes;
		this.biome = biomeSource.getNoiseBiome(0, 0, 0);
		this.random = new SharedSeedRandom(this.seed);

		// NOISEE
//		this.surfaceNoise = (new PerlinNoiseGenerator(this.random, IntStream.rangeClosed(-3, 0)));
		this.minLimitPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
		this.maxLimitPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
		this.mainPerlinNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-7, 0));
		this.random.consumeCount(2600);
		this.depthNoise = new OctavesNoiseGenerator(this.random, IntStream.rangeClosed(-15, 0));
		if (noisesettings.islandNoiseOverride()) {
			this.random.consumeCount(17292);
			this.islandNoise = new SimplexNoiseGenerator(this.random);
		} else {
			this.islandNoise = null;
		}
		this.caveDecoNoise = new OpenSimplex2F(this.seed);

	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		this.seed = seed;
		return this;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk chunk) {

//		ConfiguredSurfaceBuilder<SurfaceBuilderConfig> surf = new ConfiguredSurfaceBuilder<SurfaceBuilderConfig>(
//				new DynamicDimensionNoiseSurfaceBuilder(SurfaceBuilderConfig.CODEC, baseBlock, topLayer),
//				new SurfaceBuilderConfig(baseBlock, baseBlock, baseBlock));

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
					chunk.setBlockState(blockpos$mutable, column.getBlockState(blockpos$mutable), false);

//					if (column.getBlockState(blockpos$mutable.above()).getBlock().equals(Blocks.AIR)) {
//						if (!column.getBlockState(blockpos$mutable).getBlock().equals(Blocks.AIR)) {
//
//							double d1 = surfaceNoise.getSurfaceNoiseValue(k1 * 0.0625D, 0.0625D, l1 * 0.0625D, 0.0625D)
//									* 15.0D;
//
//							surf.apply(this.random, chunk, null, i1, j1, y, d1, baseBlock,
//									Blocks.WATER.defaultBlockState(), seaLevel, seed);
//						}
//					}
				}
			}
		}

		this.placeBedrock(chunk, this.random);
	}

	public boolean isIslands() {
		return this.noisesettings.equals(PlanetNoiseSettings.ISLAND_TYPE);
	}

	public boolean isLayered() {
		return this.noisesettings.equals(PlanetNoiseSettings.LAYERED_TYPE);
	}

	protected BlockState generateBaseState(double chance, int y) {

		int newSea = seaLevel;
		if (isIslands())
			newSea *= 0;
		else if (isLayered())
			newSea *= 2;

		BlockState blockstate;
		if (chance > 0.0D) {
			blockstate = baseBlocks.getBaseBlock();
		} else if (frozen && y == newSea - 1) {
			blockstate = fluidBlocks.getSecondaryBlock();
		} else {
			blockstate = y < newSea ? fluidBlocks.getBaseBlock() : AIR;
		}

		return blockstate;
	}

	@Override
	public void applyBiomeDecoration(WorldGenRegion region, StructureManager pStructureManager) {

		BlockPos pos = new BlockPos(region.getCenterX() * 16, 0, region.getCenterZ() * 16);

		for (int i3 = 0; i3 < 16; ++i3) {
			for (int j3 = 0; j3 < 16; ++j3) {
				for (Supplier<ConfiguredFeature<?, ?>> supplier : this.features) {
					ConfiguredFeature<?, ?> configuredfeature = supplier.get();

					try {
						configuredfeature.place(region, this, this.random, pos);
					} catch (Exception exception1) {
						CrashReport crashreport1 = CrashReport.forThrowable(exception1, "Feature placement");
						crashreport1.addCategory("Feature")
								.setDetail("Id", Registry.FEATURE.getKey(configuredfeature.feature))
								.setDetail("Config", configuredfeature.config).setDetail("Description", () -> {
									return configuredfeature.feature.toString();
								});
						throw new ReportedException(crashreport1);
					}
				}
			}
		}

		super.applyBiomeDecoration(region, pStructureManager);
	}

	@Override
	public void applyCarvers(long pSeed, BiomeManager pBiomeManager, IChunk pChunk, Carving pCarving) {

		Function<BlockPos, Biome> getBiome = (pos) -> biome;
		ChunkPos chunkpos = pChunk.getPos();
		int j = chunkpos.x;
		int k = chunkpos.z;
		BitSet bitset = ((ChunkPrimer) pChunk).getOrCreateCarvingMask(pCarving);

		for (int l = j - 8; l <= j + 8; ++l) {
			for (int i1 = k - 8; i1 <= k + 8; ++i1) {
				int l1 = 0;
				for (Supplier<ConfiguredCarver<?>> supplier : this.carvers) {
					ConfiguredCarver<?> configuredcarver = supplier.get();
					this.random.setLargeFeatureSeed(pSeed + (long) l1, l, i1);
					configuredcarver.carve(pChunk, getBiome, this.random, this.getSeaLevel(), l, i1, j, k, bitset);
					if (configuredcarver.isStartChunk(this.random, l, i1)) {
						configuredcarver.carve(pChunk, getBiome, this.random, this.getSeaLevel(), l, i1, j, k, bitset);
					}
					l1++;
				}
			}
		}

		// Add cave decoration
		for (int a = 0; a < bitset.length(); ++a) {
			if (bitset.get(a)) {
				BlockPos pos = new BlockPos(chunkpos.getMinBlockX() + (a & 15), a >> 8,
						chunkpos.getMinBlockZ() + (a >> 4 & 15));
				PlanetCaveDecorator.decorateCavesAt(pChunk, pos, this);
			}
		}

	}

	private void placeBedrock(IChunk chunk, Random random) {

		boolean layered = isLayered();

		if (!isIslands()) {
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
			int i = chunk.getPos().getMinBlockX();
			int j = chunk.getPos().getMinBlockZ();
			for (BlockPos blockpos : BlockPos.betweenClosed(i, 0, j, i + 15, 0, j + 15)) {
				for (int k1 = 4; k1 >= 0; --k1) {
					if (k1 <= random.nextInt(5)) {
						chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k1, blockpos.getZ()), BEDROCK, false);

						if (layered) {
							chunk.setBlockState(
									blockpos$mutable.set(blockpos.getX(), getGenDepth() - k1 - 1, blockpos.getZ()),
									BEDROCK, false);
						}
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
		double height;
		double heightMul;
		if (this.islandNoise != null) {
			height = (double) (EndBiomeProvider.getHeightValue(this.islandNoise, x, z) - 8.0F);
			heightMul = height > 0.0D ? 0.25D : 1.0D;
		} else {
			float f = 0.0F;
			float f1 = 0.0F;
			float f2 = 0.0F;

			for (int k = -2; k <= 2; ++k) {
				for (int l = -2; l <= 2; ++l) {
					float depth = 0f;
					float scale = 1f;
					float f9 = BIOME_WEIGHTS[k + 2 + (l + 2) * 5] / (depth + 2.0F);
					f += scale * f9;
					f1 += depth * f9;
					f2 += f9;
				}
			}

			height = (double) (f1 / f2 * 0.5F - 0.125F) * 0.265625D;
			heightMul = 96.0D / (double) (f / f2 * 0.9F + 0.1F);
		}
		double randDensity = this.randDensityEnabled ? this.getRandomDensity(x, z) : 0.0D;

		for (int i1 = 0; i1 <= getGenDepth() / this.chunkHeight; ++i1) {
			double noise = this.sampleAndClampNoise(x, i1, z, this.xzscale, this.yscale, this.xzfactor, this.yfactor);
			double rand = 1.0D - (double) i1 * 2.0D / (double) (getGenDepth() / this.chunkHeight) + randDensity;
			double totalHeight = (rand * this.randFactor + this.randOffset + height) * heightMul;
			noise += totalHeight > 0.0D ? totalHeight * 4.0D : totalHeight;

			if (this.topSize > 0.0D) {
				double d11 = ((double) (getGenDepth() / this.chunkHeight - i1) - this.topOffset) / this.topSize;
				noise = MathHelper.clampedLerp(this.topTarget, noise, d11);
			}

			if (this.botSize > 0.0D) {
				double d22 = ((double) i1 - this.botOffset) / this.botSize;
				noise = MathHelper.clampedLerp(this.botTarget, noise, d22);
			}

			out[i1] = noise;
		}

	}

	private double getRandomDensity(int x, int z) {
		double noise = this.depthNoise.getValue((double) (x * 200), 10.0D, (double) (z * 200), 1.0D, 0.0D, true);
		double d2 = (noise < 0.0D ? -noise * 0.3D : noise) * 24.575625D - 2.0D;
		return d2 < 0.0D ? d2 * 0.009486607142857142D : Math.min(d2, 1.0D) * 0.006640625D;
	}

	private double sampleAndClampNoise(int x, int y, int z, double xzSize, double ySize, double xzFactor,
			double yFactor) {
		double minNoise = 0.0D;
		double maxNoise = 0.0D;
		double mainNoise = 0.0D;
		double scale = 1.0D;

		for (int i = 0; i < 16; ++i) {
			double xWrapped = OctavesNoiseGenerator.wrap((double) x * xzSize * scale);
			double yWrapped = OctavesNoiseGenerator.wrap((double) y * ySize * scale);
			double zWrapped = OctavesNoiseGenerator.wrap((double) z * xzSize * scale);
			double wWrapped = ySize * scale;
			ImprovedNoiseGenerator minGen = this.minLimitPerlinNoise.getOctaveNoise(i);
			if (minGen != null) {
				minNoise += minGen.noise(xWrapped, yWrapped, zWrapped, wWrapped, (double) y * wWrapped) / scale;
			}

			ImprovedNoiseGenerator maxGen = this.maxLimitPerlinNoise.getOctaveNoise(i);
			if (maxGen != null) {
				maxNoise += maxGen.noise(xWrapped, yWrapped, zWrapped, wWrapped, (double) y * wWrapped) / scale;
			}

			if (i < 8) {
				ImprovedNoiseGenerator mainGen = this.mainPerlinNoise.getOctaveNoise(i);
				if (mainGen != null) {
					mainNoise += mainGen.noise(OctavesNoiseGenerator.wrap((double) x * xzFactor * scale),
							OctavesNoiseGenerator.wrap((double) y * yFactor * scale),
							OctavesNoiseGenerator.wrap((double) z * xzFactor * scale), yFactor * scale,
							(double) y * yFactor * scale) / scale;
				}
			}

			scale /= 2.0D;
		}

		return MathHelper.clampedLerp(minNoise / 512.0D, maxNoise / 512.0D, (mainNoise / 10.0D + 1.0D) / 2.0D);
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
				BlockState blockstate = this.generateBaseState(d11, y);
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
}
