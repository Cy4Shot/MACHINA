package com.cy4.machina.world;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class DynamicDimensionChunkGenerator extends ChunkGenerator {
	public static final Codec<DynamicDimensionChunkGenerator> CODEC = RegistryLookupCodec
			.create(Registry.BIOME_REGISTRY)
			.xmap(DynamicDimensionChunkGenerator::new, DynamicDimensionChunkGenerator::getBiomeRegistry).codec();

	private final Registry<Biome> biomes;

	public Registry<Biome> getBiomeRegistry() {
		return this.biomes;
	}

	public DynamicDimensionChunkGenerator(MinecraftServer server) {
		this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
	}

	public DynamicDimensionChunkGenerator(Registry<Biome> biomes) {
		super(new SingleBiomeProvider(biomes.getOrThrow(Biomes.PLAINS)), new DimensionStructuresSettings(false));
		this.biomes = biomes;
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long p_230349_1_) {
		return this;
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk chunk) {
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
