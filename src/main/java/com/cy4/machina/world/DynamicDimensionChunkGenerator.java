package com.cy4.machina.world;

import java.util.Random;
import java.util.stream.IntStream;

import com.cy4.machina.Machina;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class DynamicDimensionChunkGenerator extends ChunkGenerator {
	public static final Codec<DynamicDimensionChunkGenerator> CODEC = RegistryLookupCodec
			.create(Registry.BIOME_REGISTRY)
			.xmap(DynamicDimensionChunkGenerator::new, DynamicDimensionChunkGenerator::getBiomeRegistry).codec();

	private final Registry<Biome> biomes;
	private final INoiseGenerator surfaceNoise;
	private final int height = 60;

	public Registry<Biome> getBiomeRegistry() {
		return this.biomes;
	}

	public DynamicDimensionChunkGenerator(MinecraftServer server) {
		this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
	}

	public DynamicDimensionChunkGenerator(Registry<Biome> biomes) {
		super(new SingleBiomeProvider(biomes.getOrThrow(
				RegistryKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Machina.MOD_ID, Machina.MOD_ID)))),
				new DynamicStructureSettings());
		this.biomes = biomes;
		this.surfaceNoise = (INoiseGenerator) (new PerlinNoiseGenerator(new SharedSeedRandom(),
				IntStream.rangeClosed(-3, 0)));
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return this;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk chunk) {
		ChunkPos chunkpos = chunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setBaseChunkSeed(i, j);
		ChunkPos chunkpos1 = chunk.getPos();
		int k = chunkpos1.getMinBlockX();
		int l = chunkpos1.getMinBlockZ();
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		for (int i1 = 0; i1 < 16; ++i1) {
			for (int j1 = 0; j1 < 16; ++j1) {
				int k1 = k + i1;
				int l1 = l + j1;
				double d1 = this.surfaceNoise.getSurfaceNoiseValue((double) k1 * 0.0625D, (double) l1 * 0.0625D,
						0.0625D, (double) i1 * 0.0625D) * 15.0D;
				chunk.setBlockState(blockpos$mutable.set(i1, height + (int) d1, j1), Blocks.DIRT.defaultBlockState(),
						false);
				System.out.println(d1);
			}
		}

		this.placeBedrock(chunk, sharedseedrandom);
	}

	private void placeBedrock(IChunk chunk, Random random) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = chunk.getPos().getMinBlockX();
		int j = chunk.getPos().getMinBlockZ();
		for (BlockPos blockpos : BlockPos.betweenClosed(i, 0, j, i + 15, 0, j + 15)) {
			for (int k1 = 4; k1 >= 0; --k1) {
				if (k1 <= random.nextInt(5)) {
					chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k1, blockpos.getZ()),
							Blocks.BEDROCK.defaultBlockState(), false);
				}
			}

		}
	}

	@Override
	public void fillFromNoise(IWorld world, StructureManager structures, IChunk chunk) {
	}

	@Override
	public int getBaseHeight(int x, int z, Type heightmapType) {
		return 0;
	}

	// get base column
	@Override
	public IBlockReader getBaseColumn(int x, int z) {
		return new Blockreader(new BlockState[0]);
	}
}
