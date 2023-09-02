package com.machina.datagen.server;

import java.util.List;
import java.util.Set;

import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

// TODO: Fix this class. Make a datagen for loot tables.
public class DatagenLootTables extends BlockLootSubProvider {
	public DatagenLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		
		// Fluids
		for (FluidObject obj : FluidInit.OBJS) {
			if (!obj.gas)
				this.dropNone(obj.block());
		}
	}

	private void dropNone(Block b) {
		this.add(b, noDrop());
	}

	public static LootTableProvider create(PackOutput output) {
		return new LootTableProvider(output, Set.of(),
				List.of(new LootTableProvider.SubProviderEntry(DatagenLootTables::new, LootContextParamSets.BLOCK)));
	}
}