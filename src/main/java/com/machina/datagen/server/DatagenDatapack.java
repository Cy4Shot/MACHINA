package com.machina.datagen.server;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.machina.Machina;
import com.machina.world.overworld.MachinaBiomeModifiers;
import com.machina.world.overworld.MachinaConfiguredFeatures;
import com.machina.world.overworld.MachinaPlacedFeatures;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DatagenDatapack extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, MachinaConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, MachinaPlacedFeatures::bootstrap)
			.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MachinaBiomeModifiers::bootstrap);

	public DatagenDatapack(PackOutput po, CompletableFuture<HolderLookup.Provider> lookup) {
		super(po, lookup, BUILDER, Set.of(Machina.MOD_ID));
	}
}
