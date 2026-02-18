package com.machina.world.biome;

import java.util.Arrays;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBush;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeOre;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.PlanetSurface.PlanetSurfaceGetter;
import com.machina.registration.init.RegistryInit;
import com.machina.registration.init.SoundInit;
import com.machina.world.feature.PlanetBigRockFeature;
import com.machina.world.feature.PlanetBushFeature;
import com.machina.world.feature.PlanetCaveSlopeFeature;
import com.machina.world.feature.PlanetCaveSlopeFeature.PlanetCaveSlopeFeatureConfig;
import com.machina.world.feature.PlanetGrassFeature;
import com.machina.world.feature.PlanetGrassFeature.PlanetGrassFeatureConfig;
import com.machina.world.feature.PlanetLakeFeature;
import com.machina.world.feature.PlanetOreFeature;
import com.machina.world.feature.PlanetRockFeature;
import com.machina.world.feature.PlanetTreeFeature;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class PlanetBiome extends Biome {

	public final PlanetBiomeSettings settings;
	public final PlanetSurfaceGetter surface;

	public PlanetBiome(PlanetBiomeSettings s) {
		this(createClimate(s), createEffects(s), createGeneration(s), s);
	}

	private PlanetBiome(ClimateSettings climate, BiomeSpecialEffects special, BiomeGenerationSettings genset,
			PlanetBiomeSettings s) {
		super(climate, special, genset, MobSpawnSettings.EMPTY);
		this.settings = s;
		this.surface = RegistryInit.SURFACE_REGISTRY.get(s.surface()).create(settings.top());
	}

	public BlockState getSecondBlock() {
		return settings.second();
	}

	public BlockState getThirdBlock() {
		return settings.base();
	}

	private static ClimateSettings createClimate(PlanetBiomeSettings s) {
		boolean rains = false;
		return new ClimateSettings(rains, 1.0f, TemperatureModifier.NONE, 0f);
	}

	private static BiomeSpecialEffects createEffects(PlanetBiomeSettings s) {
		// TODO: Add particles & effects
		return s.effects().build().backgroundMusic(SoundInit.asMusic(SoundInit.MUSIC)).build();
	}

	private static BiomeGenerationSettings createGeneration(PlanetBiomeSettings s) {
		BiomeGenerationSettings.PlainBuilder builder = new BiomeGenerationSettings.PlainBuilder();

		addUndergroundFeatures(builder, s);
		addVegetationFeatures(builder, s);

		return builder.build();
	}

	private static void addUndergroundFeatures(BiomeGenerationSettings.PlainBuilder builder, PlanetBiomeSettings s) {

		// Cave Slope
		for (CaveSurface surf : CaveSurface.values())
			add(builder, Decoration.UNDERGROUND_DECORATION, new PlanetCaveSlopeFeature(),
					new PlanetCaveSlopeFeatureConfig(surf, s), count(256), spread(), range(-64, 320),
					EnvironmentScanPlacement.scanningFor(surf.getDirection(), BlockPredicate.solid(),
							BlockPredicate.matchesBlocks(Blocks.AIR), 12),
					biome());

		// Ores
		for (PlanetBiomeOre ore : s.ores()) {
			add(builder, Decoration.UNDERGROUND_ORES, new PlanetOreFeature(),
					new PlanetOreFeature.PlanetOreFeatureConfig(ore), chance(ore.chance()), spread(),
					range(ore.min_y(), ore.max_y()), biome());
		}
	}

	private static void addVegetationFeatures(BiomeGenerationSettings.PlainBuilder builder, PlanetBiomeSettings s) {

		if (s.lakes().enabled()) {
			add(builder, Decoration.LAKES, new PlanetLakeFeature(),
					new PlanetLakeFeature.PlanetLakeFeatureConfig(s.lakes().base(), s.lakes().decorator_chance(),
							s.lakes().provider()),
					chance(s.lakes().chance()), spread(), onSurface(), biome());
		}

		for (PlanetBiomeBigRock rock : s.big_rocks()) {
			add(builder, Decoration.SURFACE_STRUCTURES, new PlanetBigRockFeature(),
					new PlanetBigRockFeature.PlanetBigRockFeatureConfig(rock), chance(rock.chance()), spread(),
					onFloor(), biome());
		}

		for (PlanetBiomeTree tree : s.trees()) {
			add(builder, Decoration.SURFACE_STRUCTURES, new PlanetTreeFeature(),
					new PlanetTreeFeature.PlanetTreeFeatureConfig(tree), chance(tree.chance()), spread(), onFloor(),
					biome());
		}

		for (PlanetBiomeBush bush : s.bushes()) {
			add(builder, Decoration.VEGETAL_DECORATION, new PlanetBushFeature(),
					new PlanetBushFeature.PlanetBushFeatureConfig(bush), chance(bush.chance()), spread(), onFloor(),
					biome());
		}

		if (s.grass().enabled()) {
			add(builder, Decoration.VEGETAL_DECORATION, new PlanetGrassFeature(),
					new PlanetGrassFeatureConfig(s.grass().provider()),
					NoiseThresholdCountPlacement.of(-0.8f, s.grass().min(), s.grass().max()), spread(), onSurface(),
					biome());
		}

		for (PlanetBiomeRock rock : s.rocks()) {
			add(builder, Decoration.VEGETAL_DECORATION, new PlanetRockFeature(),
					new PlanetRockFeature.PlanetRockFeatureConfig(rock), chance(rock.chance()), spread(), onSurface(),
					biome());
		}
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void add(
			BiomeGenerationSettings.PlainBuilder builder, Decoration decorationLevel, F feature, FC config,
			PlacementModifier... placements) {
		builder.addFeature(decorationLevel, Holder.direct(
				new PlacedFeature(Holder.direct(new ConfiguredFeature<>(feature, config)), Arrays.asList(placements))));
	}

	private static PlacementModifier chance(float chance) {
		return chance > 1 ? count((int) chance) : every(chance == 0 ? 0 : (int) (1 / chance));
	}

	private static RarityFilter every(int every) {
		return RarityFilter.onAverageOnceEvery(every);
	}

	private static CountPlacement count(int count) {
		return CountPlacement.of(count);
	}

	private static InSquarePlacement spread() {
		return InSquarePlacement.spread();
	}

	private static HeightmapPlacement onFloor() {
		return HeightmapPlacement.onHeightmap(Types.OCEAN_FLOOR_WG);
	}

	private static HeightmapPlacement onSurface() {
		return HeightmapPlacement.onHeightmap(Types.WORLD_SURFACE_WG);
	}

	private static BiomeFilter biome() {
		return BiomeFilter.biome();
	}

	private static HeightRangePlacement range(int min, int max) {
		return HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(min), VerticalAnchor.belowTop(max));
	}
}