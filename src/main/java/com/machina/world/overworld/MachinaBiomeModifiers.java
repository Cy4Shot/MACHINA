package com.machina.world.overworld;

import com.machina.registration.init.OverworldOresInit;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MachinaBiomeModifiers {
	public static void bootstrap(BootstrapContext<BiomeModifier> ctx) {
		HolderGetter<PlacedFeature> placedFeatures = ctx.lookup(Registries.PLACED_FEATURE);
		HolderGetter<Biome> biomes = ctx.lookup(Registries.BIOME);

		OverworldOresInit.ORES.forEach(ore -> {
			ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ore.loc()),
					new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
							HolderSet.direct(placedFeatures
									.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE, ore.loc()))),
							GenerationStep.Decoration.UNDERGROUND_ORES));
		});
	}
}
