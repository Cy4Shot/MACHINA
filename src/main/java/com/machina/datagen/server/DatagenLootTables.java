package com.machina.datagen.server;

import java.util.List;
import java.util.Set;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.ItemInit;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

// TODO: Fix this class. Make a datagen for loot tables.
public class DatagenLootTables extends BlockLootSubProvider {
	public DatagenLootTables() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {

		// Drop Self
		this.dropSelf(BlockInit.ALUMINUM_BLOCK.get());

		// Ore
		this.dropOre(BlockInit.ALUMINUM_ORE.get(), ItemInit.RAW_ALUMINUM.get());

		// Fluids
		for (FluidObject obj : FluidInit.OBJS) {
			if (!obj.gas)
				this.dropNone(obj.block());
		}
	}

	private void dropNone(Block b) {
		this.add(b, noDrop());
	}

	private void dropOre(Block b, Item i) {
		this.add(b, createSilkTouchDispatchTable(b, this.applyExplosionDecay(b,
				LootItem.lootTableItem(i).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
	}

	@SuppressWarnings("unused")
	private void dropOre(Block b, Item i, int min, int max) {
		this.add(b,
				createSilkTouchDispatchTable(b,
						this.applyExplosionDecay(b,
								LootItem.lootTableItem(i)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
										.apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
	}

	public static LootTableProvider create(PackOutput output) {
		return new LootTableProvider(output, Set.of(),
				List.of(new LootTableProvider.SubProviderEntry(DatagenLootTables::new, LootContextParamSets.BLOCK)));
	}
}