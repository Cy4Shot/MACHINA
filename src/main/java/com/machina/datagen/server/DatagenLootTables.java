package com.machina.datagen.server;

import java.util.Set;

import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.DirtFamily;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.FruitInit;
import com.machina.registration.init.FruitInit.Fruit;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
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

public class DatagenLootTables extends BlockLootSubProvider {

	private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[] { 0.02F, 0.022222223F, 0.025F, 0.033333335F,
			0.1F };

	public DatagenLootTables(HolderLookup.Provider lookupProvider) {
		super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BlockInit.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
	}

	@Override
	protected void generate() {
		dropSelf(BlockInit.ENERGY_CABLE.get());
		dropSelf(BlockInit.FLUID_PIPE.get());
		dropSelf(BlockInit.ITEM_CONDUIT.get());

		dropSelf(BlockInit.BASIC_CASING.get());
		dropSelf(BlockInit.LIGHTWEIGHT_CASING.get());

		dropSelf(BlockInit.BATTERY.get());
		dropSelf(BlockInit.TANK.get());
		dropSelf(BlockInit.CREATIVE_BATTERY.get());
		dropSelf(BlockInit.FURNACE_GENERATOR.get());
		dropSelf(BlockInit.CHEMICAL_GENERATOR.get());
		dropSelf(BlockInit.ELECTRIC_SMELTER.get());
		dropSelf(BlockInit.GRINDER.get());
		dropSelf(BlockInit.COMPRESSOR.get());
		dropSelf(BlockInit.MELTER.get());
		dropSelf(BlockInit.SOLIDIFIER.get());
		dropSelf(BlockInit.REACTION_CHAMBER.get());
		dropSelf(BlockInit.COMPOSTER_VAT.get());
		dropSelf(BlockInit.SAWMILL.get());
		dropSelf(BlockInit.ELECTROLYZER.get());
		dropSelf(BlockInit.ELECTRIC_PUMP.get());
		dropSelf(BlockInit.ATMOSPHERIC_SEPARATOR.get());
		dropSelf(BlockInit.ROCKET_PART_BENCH.get());
		dropSelf(BlockInit.ROCKET_ASSEMBLY_STATION.get());
		dropSelf(BlockInit.ROCKET_REFUELING_STATION.get());
		dropSelf(BlockInit.GEOTHERMAL_GENERATOR_CONTROLLER.get());
		dropSelf(BlockInit.GEOTHERMAL_GENERATOR_CASING.get());
		dropSelf(BlockInit.GEOTHERMAL_SUPPORT_ROD.get());

		dropSelf(BlockInit.BROWN_MUSHROOM_STALK.get());
		dropSelf(BlockInit.GREEN_MUSHROOM_STALK.get());
		dropSelf(BlockInit.PURPLE_MUSHROOM_STALK.get());
		dropSelf(BlockInit.YELLOW_MUSHROOM_STALK.get());
		dropSelf(BlockInit.BROWN_MUSHROOM_CAP.get());
		dropSelf(BlockInit.RED_MUSHROOM_CAP.get());
		dropSelf(BlockInit.PURPLE_MUSHROOM_CAP.get());
		dropSelf(BlockInit.YELLOW_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.BROWN_MUSHROOM_GILLS.get(), BlockInit.BROWN_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.RED_MUSHROOM_GILLS.get(), BlockInit.RED_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.PURPLE_MUSHROOM_GILLS.get(), BlockInit.PURPLE_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.YELLOW_MUSHROOM_GILLS.get(), BlockInit.YELLOW_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.SPECKLED_BROWN_MUSHROOM_CAP.get(), BlockInit.BROWN_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.SPECKLED_RED_MUSHROOM_CAP.get(), BlockInit.RED_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.SPECKLED_PURPLE_MUSHROOM_CAP.get(), BlockInit.PURPLE_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.SPECKLED_YELLOW_MUSHROOM_CAP.get(), BlockInit.YELLOW_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.IMBUED_BROWN_MUSHROOM_CAP.get(), BlockInit.BROWN_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.IMBUED_RED_MUSHROOM_CAP.get(), BlockInit.RED_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.IMBUED_PURPLE_MUSHROOM_CAP.get(), BlockInit.PURPLE_MUSHROOM_CAP.get());
		dropAsSilkOr(BlockInit.IMBUED_YELLOW_MUSHROOM_CAP.get(), BlockInit.YELLOW_MUSHROOM_CAP.get());

		dropSelf(BlockInit.PURPLE_GLOWSHROOM.get());
		pot(BlockInit.POTTED_PURPLE_GLOWSHROOM.get());
		dropSelf(BlockInit.PINK_GLOWSHROOM.get());
		pot(BlockInit.POTTED_PINK_GLOWSHROOM.get());
		dropSelf(BlockInit.RED_GLOWSHROOM.get());
		pot(BlockInit.POTTED_RED_GLOWSHROOM.get());
		dropSelf(BlockInit.ORANGE_GLOWSHROOM.get());
		pot(BlockInit.POTTED_ORANGE_GLOWSHROOM.get());
		dropSelf(BlockInit.YELLOW_GLOWSHROOM.get());
		pot(BlockInit.POTTED_YELLOW_GLOWSHROOM.get());
		dropSelf(BlockInit.GREEN_GLOWSHROOM.get());
		pot(BlockInit.POTTED_GREEN_GLOWSHROOM.get());
		dropSelf(BlockInit.TURQUOISE_GLOWSHROOM.get());
		pot(BlockInit.POTTED_TURQUOISE_GLOWSHROOM.get());
		dropSelf(BlockInit.BLUE_GLOWSHROOM.get());
		pot(BlockInit.POTTED_BLUE_GLOWSHROOM.get());

		dropSelf(BlockInit.SPRUCE_CUP.get());
		pot(BlockInit.POTTED_SPRUCE_CUP.get());
		dropSelf(BlockInit.DRAGON_PEONY.get());
		pot(BlockInit.POTTED_DRAGON_PEONY.get());
		dropAsSilkShears(BlockInit.SPINDLESPROUT.get());
		pot(BlockInit.POTTED_SPINDLESPROUT.get());
		dropAsSilkShears(BlockInit.SMALL_FERN.get());
		pot(BlockInit.POTTED_SMALL_FERN.get());
		dropAsSilkShears(BlockInit.DEAD_SMALL_FERN.get());
		pot(BlockInit.POTTED_DEAD_SMALL_FERN.get());
		dropAsSilkShears(BlockInit.NEEDLEGRASS.get());
		pot(BlockInit.POTTED_NEEDLEGRASS.get());

		dropAsSilkShears(BlockInit.SPINDLEGRASS.get());
		dropAsSilkShears(BlockInit.NEEDLETHATCH.get());
		dropSelf(BlockInit.ORPHEUM.get());
		dropAsSilkShears(BlockInit.CLOVER.get());
		dropAsSilkShears(BlockInit.TROPICAL_GRASS.get());
		dropAsSilkShears(BlockInit.TWISTED_GRASS.get());
		dropAsSilkShears(BlockInit.CONIFEROUS_GRASS.get());
		dropAsSilkShears(BlockInit.SHORT_CONIFEROUS_GRASS.get());
		dropAsSilkShears(BlockInit.WINDSWEPT_GRASS.get());
		dropAsSilkShears(BlockInit.MYCELIAL_GRASS.get());
		dropAsSilkShears(BlockInit.FERROUS_GRASS.get());
		dropAsSilkShears(BlockInit.MOONGRASS.get());
		petals(BlockInit.PURPLE_PETALS.get());
		petals(BlockInit.RED_PETALS.get());
		petals(BlockInit.ORANGE_PETALS.get());
		petals(BlockInit.YELLOW_PETALS.get());
		petals(BlockInit.GREEN_PETALS.get());
		petals(BlockInit.TURQUOISE_PETALS.get());
		petals(BlockInit.BLUE_PETALS.get());

		dropAsSilkShears(BlockInit.PURPLE_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.PINK_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.RED_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.ORANGE_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.YELLOW_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.GREEN_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.TURQUOISE_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.BLUE_GROUNDLILY.get());
		dropAsSilkShears(BlockInit.PURPLE_WATERLILY.get());
		dropAsSilkShears(BlockInit.PINK_WATERLILY.get());
		dropAsSilkShears(BlockInit.RED_WATERLILY.get());
		dropAsSilkShears(BlockInit.ORANGE_WATERLILY.get());
		dropAsSilkShears(BlockInit.YELLOW_WATERLILY.get());
		dropAsSilkShears(BlockInit.GREEN_WATERLILY.get());
		dropAsSilkShears(BlockInit.TURQUOISE_WATERLILY.get());
		dropAsSilkShears(BlockInit.BLUE_WATERLILY.get());

		// Fruit
		for (Fruit fruit : FruitInit.FRUITS) {
			dropSelf(fruit.block().get());
		}

		FamiliesInit.ORES.forEach(this::oreFamily);
		FamiliesInit.DIRTS.forEach(this::dirtFamily);
		FamiliesInit.STONES.forEach(this::stoneFamily);
		FamiliesInit.WOODS.forEach(this::woodFamily);

		dropSelf(BlockInit.TROPICAL_SAND.get());
		dropSelf(BlockInit.MOONSAND.get());
		dropSelf(BlockInit.FERROUS_SAND.get());
		dropSelf(BlockInit.ASH.get());
		dropSelf(BlockInit.POLLUTED_SAND.get());
		dropSelf(BlockInit.TOXIC_SAND.get());

		dropOther(BlockInit.SULFUR_GEYSER.get(), BlockInit.BASALT);
	}

	private void oreFamily(OreFamily family) {
		family.getBlock().ifPresent(this::dropSelf);
		family.getRawBlock().ifPresent(this::dropSelf);
		family.ore().ifPresent(ore -> family.raw().ifPresentOrElse(raw -> {
			ore.map().values().forEach(o -> ore(o.get(), raw));
		}, () -> family.ingot().ifPresent(ingot -> {
			ore.map().values().forEach(o -> ore(o.get(), ingot));
		})));
		family.getIngot().ifPresent(ingot -> {
			if (ingot instanceof BlockItem blockIngot) {
				this.dropSelf(blockIngot.getBlock());
			}
		});
	}

	private void dirtFamily(DirtFamily family) {
		dropSelf(family.dirt());
		dropSelf(family.stairs());
		dropSelf(family.slab());
		family.grass().ifPresent(grass -> dropWithSilk(grass, family.dirt()));
	}

	private void stoneFamily(StoneFamily family) {
		dropSelf(family.base());
		dropSelf(family.stairs());
		dropSelf(family.wall());
		dropSelf(family.button());
		dropSelf(family.pressure_plate());
		dropSelf(family.pebbles());
		slab(family.slab());
	}

	private void woodFamily(WoodFamily family) {
		dropSelf(family.button());
		dropSelf(family.door());
		dropSelf(family.fence());
		dropSelf(family.fencegate());
		dropSelf(family.hangingsignblock());
		dropSelf(family.log());
		dropSelf(family.planks());
		dropSelf(family.pressure_plate());
		dropSelf(family.signblock());
		dropSelf(family.stairs());
		dropSelf(family.trapdoor());
		dropSelf(family.wallsignblock());
		dropSelf(family.hangingwallsignblock());
		dropSelf(family.wood());
		dropSelf(family.stripped_log());
		dropSelf(family.stripped_wood());
		slab(family.slab());
		for (Block leaf : family.leaves()) {
			leaves(leaf);
		}
	}

	private void slab(Block slab) {
		this.add(slab, this::createSlabItemTable);
	}

	private void pot(FlowerPotBlock pot) {
		this.add(pot, this::createPotFlowerItemTable);
	}

	private void dropAsSilkOr(Block block, ItemLike drop) {
		this.add(block,
				LootTable.lootTable()
						.withPool(LootPool.lootPool().when(hasSilkTouch()).setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(drop)))
						.withPool(LootPool.lootPool().when(doesNotHaveSilkTouch()).setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(block))));
	}

	private void dropWithSilk(Block block, ItemLike drop) {
		this.add(block, (result) -> createSingleItemTableWithSilkTouch(result, drop));
	}

	private void dropAsSilkShears(Block block) {
		this.add(block, LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS.or(hasSilkTouch()))
				.setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block))));
	}

	private void ore(Block block, Item drop) {
		this.add(block, (result) -> createOreDrop(result, drop));
	}

	private void leaves(Block block) {
		HolderLookup.RegistryLookup<Enchantment> lookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		this.add(block,
				createSilkTouchOrShearsDispatchTable(block, this
						.applyExplosionDecay(block,
								LootItem.lootTableItem(Items.STICK)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.when(BonusLevelTableCondition.bonusLevelFlatChance(lookup.getOrThrow(Enchantments.FORTUNE),
								NORMAL_LEAVES_STICK_CHANCES))));
	}

	private void petals(Block petal) {
		this.add(petal, this.createPetalsDrops(petal));
	}
}
