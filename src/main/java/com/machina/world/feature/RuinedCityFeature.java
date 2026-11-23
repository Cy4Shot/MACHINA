package com.machina.world.feature;

import java.util.List;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityGrid;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityOvergrown;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;
import com.machina.world.feature.city.CityGen;
import com.mojang.serialization.Codec;

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
			int roadWidth, int blockSize, PlanetBiomeCityOvergrown overgrown,
			PlanetBiomeCityGrid cityGrid) implements FeatureConfiguration {

		public Config(PlanetBiomeCityGrid cg) {
			this(cg.ruins(), cg.road_blocks(), cg.gradient_blocks(), cg.accent_blocks(),
					cg.accent_chance(), cg.road_smooth(), cg.slope_check(), cg.headroom(),
					cg.bridge_threshold(), cg.tunnel_threshold(), cg.max_height_diff(),
					cg.road_width(), cg.block_size(), cg.overgrown(), cg);
		}

		public static final Codec<Config> CODEC = PlanetBiomeCityGrid.CODEC.xmap(Config::new, Config::cityGrid);
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
					seed, random, roadSpacing, config.roadWidth(),
					config.overgrown(), config.cityGrid());
		}

		return true;
	}
}
