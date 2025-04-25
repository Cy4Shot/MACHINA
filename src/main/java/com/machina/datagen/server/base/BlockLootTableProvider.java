package com.machina.datagen.server.base;

import java.util.Set;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class BlockLootTableProvider extends BlockLootSubProvider {

	private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F,
			0.1F };

	protected BlockLootTableProvider() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	public void slab(Block slab) {
		this.add(slab, this::createSlabItemTable);
	}

	public void pot(FlowerPotBlock pot) {
		this.add(pot, this::createPotFlowerItemTable);
	}

	public void dropAsSilk(Block block) {
		this.dropWhenSilkTouch(block);
	}

	public void dropAsSilkOr(Block block, ItemLike drop) {
		this.add(block,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(drop)))
						.withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(block))));
	}

	public void dropWithSilk(Block block, ItemLike drop) {
		this.add(block, (result) -> createSingleItemTableWithSilkTouch(result, drop));
	}

	public void ore(Block block, Item drop) {
		this.add(block, (result) -> createOreDrop(result, drop));
	}

	public void dropNone(Block block) {
		this.add(block, noDrop());
	}

	public void leaves(Block block) {
		this.add(block, createCustomLeavesDrops(block));
	}

	private LootTable.Builder createCustomLeavesDrops(Block block) {
		return createSilkTouchOrShearsDispatchTable(block, this
				.applyExplosionDecay(block,
						LootItem.lootTableItem(Items.STICK)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
						NORMAL_LEAVES_STICK_CHANCES)));
	}
}