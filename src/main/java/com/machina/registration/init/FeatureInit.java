package com.machina.registration.init;

import com.machina.Machina;

import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeatureInit {

	@SubscribeEvent
	public static void addOres(final BiomeLoadingEvent event) {
		if (event.getCategory().equals(Biome.Category.NETHER)) {

		} else if (event.getCategory().equals(Biome.Category.THEEND)) {

		} else {
			addOre(event, FillerBlockType.NATURAL_STONE, BlockInit.ALUMINUM_ORE.get(), 4, 0, 60, 20);
		}
	}

	private static void addOre(final BiomeLoadingEvent event, RuleTest rule, Block b, int veinSize, int minHeight,
			int maxHeight, int amount) {
		ConfiguredFeature<?, ?> ore = register("ore",
				Feature.ORE.configured(new OreFeatureConfig(rule, b.defaultBlockState(), veinSize))
						.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minHeight, 0, maxHeight)))
						.squared().count(amount));
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name,
			ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Machina.MOD_ID + ":" + name, configuredFeature);
	}
}
