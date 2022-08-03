package com.machina.registration.init;

import com.machina.util.text.MachinaRL;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredFeatureInit {
	public static StructureFeature<?, ?> CONFIGURED_SHIP = StructureInit.SHIP.get().configured(IFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new MachinaRL("configured_ship"), CONFIGURED_SHIP);
        FlatGenerationSettings.STRUCTURE_FEATURES.put(StructureInit.SHIP.get(), CONFIGURED_SHIP);
    }
}
