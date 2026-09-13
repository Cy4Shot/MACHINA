package com.machina.world.feature;

import java.util.List;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;
import com.machina.world.feature.city.CityGen;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Ruined cyberpunk city feature.
 *
 * Runs once per chunk. Each chunk independently determines whether it's a
 * road or building chunk using deterministic math on chunk coordinates.
 * Roads form a connected grid because every chunk on a grid line generates
 * its portion of the road independently.
 *
 * Inspired by Lost Cities mod architecture.
 */
public class RuinedCityFeature extends Feature<RuinedCityFeature.Config> {

	public record Config(List<PlanetBiomeRuin> ruins, List<BlockState> roadBlocks,
			List<BlockState> gradientBlocks, List<BlockState> accentBlocks,
			float accentChance, int roadSmooth, int slopeCheck, int headroom,
			int bridgeThreshold, int tunnelThreshold, int maxHeightDiff,
			int roadWidth, int blockSize) implements FeatureConfiguration {

		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.list(PlanetBiomeRuin.CODEC).fieldOf("ruins").forGetter(Config::ruins),
						Codec.list(BlockState.CODEC).fieldOf("road_blocks").forGetter(Config::roadBlocks),
						Codec.list(BlockState.CODEC).fieldOf("gradient_blocks").forGetter(Config::gradientBlocks),
						Codec.list(BlockState.CODEC).fieldOf("accent_blocks").forGetter(Config::accentBlocks),
						Codec.FLOAT.fieldOf("accent_chance").forGetter(Config::accentChance),
						Codec.INT.fieldOf("road_smooth").forGetter(Config::roadSmooth),
						Codec.INT.fieldOf("slope_check").forGetter(Config::slopeCheck),
						Codec.INT.fieldOf("headroom").forGetter(Config::headroom),
						Codec.INT.fieldOf("bridge_threshold").forGetter(Config::bridgeThreshold),
						Codec.INT.fieldOf("tunnel_threshold").forGetter(Config::tunnelThreshold),
						Codec.INT.fieldOf("max_height_diff").forGetter(Config::maxHeightDiff),
						Codec.INT.fieldOf("road_width").forGetter(Config::roadWidth),
						Codec.INT.fieldOf("block_size").forGetter(Config::blockSize))
				.apply(instance, Config::new));
	}

	public RuinedCityFeature() {
		super(Config.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		Config config = context.config();
		BlockPos origin = context.origin();
		RandomSource random = context.random();
		WorldGenLevel level = context.level();
		long seed = level.getSeed();

		// Derive chunk coordinates from wherever the placement put us
		int chunkX = origin.getX() >> 4;
		int chunkZ = origin.getZ() >> 4;

		// Road spacing in chunks (e.g., block_size=64 → every 4 chunks)
		int roadSpacing = Math.max(2, config.blockSize() / 16);

		// Determine chunk type — pure math on chunk coordinates
		boolean hasZRoad = CityGen.isZRoadChunk(chunkX, roadSpacing);
		boolean hasXRoad = CityGen.isXRoadChunk(chunkZ, roadSpacing);
		boolean isRoadChunk = hasZRoad || hasXRoad;

		// ALWAYS generate roads in road chunks
		if (isRoadChunk) {
			CityGen.generateRoads(level, chunkX, chunkZ, hasZRoad, hasXRoad,
					config.roadBlocks(), config.roadWidth(), random,
					config.roadSmooth(), config.slopeCheck(), config.headroom(),
					config.bridgeThreshold(), config.tunnelThreshold(), config.maxHeightDiff());
		}

		// Generate buildings in non-road chunks
		if (!isRoadChunk) {
			CityGen.generateBuildings(level, chunkX, chunkZ,
					config.ruins(), config.gradientBlocks(), config.accentBlocks(),
					config.accentChance(),
					seed, random, roadSpacing, config.roadWidth());
		}

		return true;
	}
}
