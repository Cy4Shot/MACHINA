package com.machina.world.feature;

import java.util.List;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityGrid;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityOvergrown;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;
import com.machina.api.starchart.planet_structure.PlanetStructure;
import com.machina.api.starchart.planet_structure.PlanetStructureLoader;
import com.machina.api.util.MachinaRL;
import com.machina.world.feature.city.CityGen;
import com.machina.world.structure.CityVaultStructure;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class RuinedCityFeature extends Feature<RuinedCityFeature.Config> {

	private static final ResourceLocation VAULT_ID = MachinaRL.create("city_vault");
	private static final int ENTRANCE_EXCLUSION = 8;

	public record Config(List<PlanetBiomeRuin> ruins, List<BlockState> roadBlocks, List<BlockState> gradientBlocks,
			List<BlockState> accentBlocks, float accentChance, int roadSmooth, int slopeCheck, int headroom,
			int bridgeThreshold, int tunnelThreshold, int maxHeightDiff, int roadWidth, int blockSize,
			PlanetBiomeCityOvergrown overgrown, PlanetBiomeCityGrid cityGrid) implements FeatureConfiguration {

		public Config(PlanetBiomeCityGrid cg) {
			this(cg.ruins(), cg.road_blocks(), cg.gradient_blocks(), cg.accent_blocks(), cg.accent_chance(),
					cg.road_smooth(), cg.slope_check(), cg.headroom(), cg.bridge_threshold(), cg.tunnel_threshold(),
					cg.max_height_diff(), cg.road_width(), cg.block_size(), cg.overgrown(), cg);
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

		// Skip chunks at vault entrance positions
		if (isVaultEntranceChunk(seed, chunkX, chunkZ)) {
			return true;
		}

		// Road spacing in chunks (e.g., block_size=64 → every 4 chunks)
		int roadSpacing = Math.max(2, config.blockSize() / 16);

		// Determine chunk type — pure math on chunk coordinates
		boolean hasZRoad = CityGen.isZRoadChunk(chunkX, roadSpacing);
		boolean hasXRoad = CityGen.isXRoadChunk(chunkZ, roadSpacing);
		boolean isRoadChunk = hasZRoad || hasXRoad;

		// Generate roads in road chunks
		if (isRoadChunk) {
			CityGen.generateRoads(level, chunkX, chunkZ, hasZRoad, hasXRoad, config.roadBlocks(), config.roadWidth(),
					random, config.roadSmooth(), config.slopeCheck(), config.headroom(), config.bridgeThreshold(),
					config.tunnelThreshold(), config.maxHeightDiff());
		}

		// Generate buildings in non-road chunks
		if (!isRoadChunk) {
			CityGen.generateBuildings(level, chunkX, chunkZ, config.ruins(), config.gradientBlocks(),
					config.accentBlocks(), config.accentChance(), seed, random, roadSpacing, config.roadWidth(),
					config.overgrown(), config.cityGrid());
		}

		return true;
	}

	private boolean isVaultEntranceChunk(long seed, int chunkX, int chunkZ) {
		PlanetStructure vault = PlanetStructureLoader.INSTANCE.get(VAULT_ID);
		if (vault == null) return false;

		int chunkCenterX = (chunkX << 4) + 8;
		int chunkCenterZ = (chunkZ << 4) + 8;

		List<BlockPos> vaultPositions = CityVaultStructure.calculateNearbyVaultPositions(
			seed, chunkX, chunkZ, vault.spacing(), vault.separation(), vault.salt(), 1
		);

		for (BlockPos vaultPos : vaultPositions) {
			int dx = Math.abs(chunkCenterX - vaultPos.getX());
			int dz = Math.abs(chunkCenterZ - vaultPos.getZ());
			if (dx <= ENTRANCE_EXCLUSION && dz <= ENTRANCE_EXCLUSION) {
				return true;
			}
		}
		return false;
	}
}
