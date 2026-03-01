package com.machina.world.overworld;

import java.util.List;

import com.machina.registration.init.OverworldOresInit;
import com.machina.registration.init.OverworldOresInit.OverworldOre;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class MachinaConfiguredFeatures {
	private static final RuleTest STONE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
	private static final RuleTest DEEPLSLATE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		OverworldOresInit.ORES.forEach(ore -> {
			registerOre(ctx, ore);
		});
	}

	private static final void registerOre(BootstrapContext<ConfiguredFeature<?, ?>> ctx, OverworldOre ore) {
		register(ctx, ResourceKey.create(Registries.CONFIGURED_FEATURE, ore.loc()), Feature.ORE, new OreConfiguration(
				List.of(OreConfiguration.target(STONE_REPLACEABLES, ore.block().get().defaultBlockState()),
						OreConfiguration.target(DEEPLSLATE_REPLACEABLES, ore.block().get().defaultBlockState())),
				ore.veinSize()));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
			BootstrapContext<ConfiguredFeature<?, ?>> ctx, ResourceKey<ConfiguredFeature<?, ?>> key, F feature,
			FC config) {
		ctx.register(key, new ConfiguredFeature<>(feature, config));
	}
}
