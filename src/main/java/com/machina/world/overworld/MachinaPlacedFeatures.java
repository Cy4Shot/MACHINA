package com.machina.world.overworld;

import java.util.List;

import com.machina.registration.init.OverworldOresInit;
import com.machina.registration.init.OverworldOresInit.OverworldOre;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class MachinaPlacedFeatures {

	public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
		OverworldOresInit.ORES.forEach(ore -> {
			registerOre(ctx, ore);
		});
	}

	private static final void registerOre(BootstrapContext<PlacedFeature> ctx, OverworldOre ore) {
		register(ctx, ResourceKey.create(Registries.PLACED_FEATURE, ore.loc()),
				ctx.lookup(Registries.CONFIGURED_FEATURE)
						.getOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE, ore.loc())),
				ore.modifiers());
	}

	private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
			Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
		context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
	}
}
