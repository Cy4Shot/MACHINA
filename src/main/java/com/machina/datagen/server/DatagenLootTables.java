package com.machina.datagen.server;

import java.util.List;
import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class DatagenLootTables extends BlockLootSubProvider {
	public DatagenLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
//		this.dropSelf(null);
	}

	public static LootTableProvider create(PackOutput output) {
		return new LootTableProvider(output, Set.of(),
				List.of(new LootTableProvider.SubProviderEntry(DatagenLootTables::new, LootContextParamSets.BLOCK)));
	}
}