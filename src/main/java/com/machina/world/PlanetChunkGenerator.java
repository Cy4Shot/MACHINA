package com.machina.world;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.PlanetHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class PlanetChunkGenerator extends ChunkGenerator {

	//@formatter:off
	public static final Codec<PlanetChunkGenerator> CODEC =
		RecordCodecBuilder.create(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(c -> c.biomeSource),
			Codec.INT.fieldOf("id").forGetter(c -> c.id),
			Codec.LONG.fieldOf("seed").forGetter(c -> c.seed))
		.apply(instance, PlanetChunkGenerator::new));
	//@formatter:on

	int id;
	Planet planet;
	long seed;

	public PlanetChunkGenerator(BiomeSource source, ResourceKey<LevelStem> dim, long seed) {
		this(source, PlanetHelper.getIdDim(dim), seed);
	}

	public PlanetChunkGenerator(BiomeSource source, int id, long seed) {
		super(source);
		this.id = id;
		this.planet = Starchart.system(seed).planets().get(id);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(WorldGenRegion p_223043_, long p_223044_, RandomState p_223045_, BiomeManager p_223046_,
			StructureManager p_223047_, ChunkAccess p_223048_, Carving p_223049_) {
	}

	@Override
	public void buildSurface(WorldGenRegion reg, StructureManager p_223051_, RandomState p_223052_, ChunkAccess ca) {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				reg.setBlock(new BlockPos(ca.getPos().x * 16 + i, 100, ca.getPos().z * 16 + j),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion p_62167_) {
	}

	@Override
	public int getGenDepth() {
		return 384;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor ex, Blender bl, RandomState ra, StructureManager sm,
			ChunkAccess ca) {
		return CompletableFuture.completedFuture(ca);
	}

	@Override
	public int getSeaLevel() {
		return 60;
	}

	@Override
	public int getMinY() {
		return -64;
	}

	@Override
	public int getBaseHeight(int p_223032_, int p_223033_, Types p_223034_, LevelHeightAccessor p_223035_,
			RandomState p_223036_) {
		return 0;
	}

	@Override
	public NoiseColumn getBaseColumn(int p_223028_, int p_223029_, LevelHeightAccessor p_223030_,
			RandomState p_223031_) {
		return null;
	}

	@Override
	public void addDebugScreenInfo(List<String> p_223175_, RandomState p_223176_, BlockPos p_223177_) {
	}
}