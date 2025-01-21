package com.machina.datagen.server;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipeBuilder;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FamiliesInit;
import com.machina.registration.init.FamiliesInit.OreFamily;
import com.machina.registration.init.FamiliesInit.StoneFamily;
import com.machina.registration.init.FamiliesInit.WoodFamily;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.RecipeInit;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class DatagenRecipes extends RecipeProvider implements IConditionBuilder {

	public DatagenRecipes(PackOutput po) {
		super(po);
	}

	@Override
	protected void buildRecipes(@NotNull Consumer<FinishedRecipe> gen) {
		ore(gen, List.of(ItemInit.RAW_ALUMINUM.get(), BlockInit.ALUMINUM_ORE.get()), ItemInit.ALUMINUM_INGOT.get(),
				0.25f, 200, "aluminum");
		ore(gen, List.of(BlockInit.ANTHRACITE.get()), ItemInit.COAL_CHUNK.get(), 0.05f, 40, "anthracite");

		compact(gen, BlockInit.ALUMINUM_BLOCK.get(), ItemInit.ALUMINUM_INGOT.get());
		compact(gen, ItemInit.ALUMINUM_INGOT.get(), ItemInit.ALUMINUM_NUGGET.get());
		compact(gen, Items.COAL, ItemInit.COAL_CHUNK.get());

		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.ANTHRACITE.get())
			.requires(Blocks.STONE)
			.requires(ItemInit.COAL_CHUNK.get())
			.unlockedBy(getHasName(BlockInit.ANTHRACITE.get()), has(BlockInit.ANTHRACITE.get()))
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.ANTHRACITE.get()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.MIGMATITE.get())
			.pattern("SG")
			.pattern("GS")
			.define('S', Blocks.STONE)
			.define('G', Blocks.GRANITE)
			.unlockedBy(getHasName(BlockInit.MIGMATITE.get()), has(BlockInit.MIGMATITE.get()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(BlockInit.MIGMATITE.get()));
		//@formatter:on

		FamiliesInit.ORES.forEach(x -> oreFamily(gen, x));
		FamiliesInit.STONES.forEach(x -> stoneFamily(gen, x));
		FamiliesInit.WOODS.forEach(x -> woodFamily(gen, x));
	}

	protected static void oreFamily(Consumer<FinishedRecipe> gen, OreFamily family) {
		// Crafting ingot
		family.getIngot().ifPresent(ingot -> {
			family.ore().ifPresent(ore -> {
				ore(gen, List.of(ore), ingot, 0.7f, 200, family.name());
			});
			ore(gen, List.of(family.dust()), ingot, 0.7f, 200, family.name());
		});

		// Crafting block
		family.getBlock().ifPresent(block -> {
			family.ingot().ifPresent(ingot -> {
				compact(gen, block, ingot);
			});
		});

		// Crafting nugget
		family.getNugget().ifPresent(nugget -> {
			family.ingot().ifPresent(ingot -> {
				compact(gen, ingot, nugget);
			});
		});

		// Crafting dust
		family.ore().ifPresent(ore -> {
			//@formatter:off
			MachinaRecipeBuilder.create(RecipeInit.GRINDER, 0.1f)
				.withEnergy(40000).withTime(300)
				.withInputItem(ore.asItem(), 1).withOutputItem(family.dust(), 2)
				.unlockedBy(getHasName(ore), has(ore))
				.save(gen, Machina.MOD_ID + ":grinder_ore_to_" + getItemName(family.dust()));
			//@formatter:on
		});
		family.ingot().ifPresent(ingot -> {
			//@formatter:off
			MachinaRecipeBuilder.create(RecipeInit.GRINDER, 0.1f)
				.withEnergy(15000).withTime(200)
				.withInputItem(ingot.asItem(), 1).withOutputItem(family.dust(), 1)
				.unlockedBy(getHasName(ingot), has(ingot))
				.save(gen, Machina.MOD_ID + ":grinder_ingot_to_" + getItemName(family.dust()));
			//@formatter:on
		});

	}

	protected static void woodFamily(Consumer<FinishedRecipe> gen, WoodFamily family) {
		//@formatter:off
		log_and_plank(gen, family.log(), family.wood(), family.planks());
		log_and_plank(gen, family.stripped_log(), family.stripped_wood(), family.planks());
		slab(gen, family.planks(), family.slab());
		stair(gen, family.planks(), family.stairs());
		door(gen, family.planks(), family.door());
		trapdoor(gen, family.planks(), family.trapdoor());
		pressure_plate(gen, family.planks(), family.pressure_plate());
		button(gen, family.planks(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.fence(), 3)
			.pattern("BSB")
			.pattern("BSB")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fence()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, family.fencegate())
			.pattern("SBS")
			.pattern("SBS")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.fencegate()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.sign(), 3)
			.pattern("BBB")
			.pattern("BBB")
			.pattern(" S ")
			.define('B', family.planks())
			.define('S', Items.STICK)
			.unlockedBy(getHasName(family.planks()), has(family.planks()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.sign()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, family.hangingsign(), 6)
			.pattern("C C")
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.stripped_log())
			.define('C', Items.CHAIN)
			.unlockedBy(getHasName(family.stripped_log()), has(family.stripped_log()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.hangingsign()));
		//@formatter:on
	}

	protected static void log_and_plank(Consumer<FinishedRecipe> gen, ItemLike log, ItemLike wood, ItemLike planks) {
		//@formatter:off
		// LOG -> WOOD
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, wood, 3)
			.pattern("BB")
			.pattern("BB")
			.define('B', log)
			.unlockedBy(getHasName(log), has(log))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(wood));
		
		// LOG -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(log)
			.unlockedBy(getHasName(log), has(log))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(log));
		
		// WOOD -> PLANKS
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, 4)
			.requires(wood)
			.unlockedBy(getHasName(wood), has(wood))
			.save(gen, Machina.MOD_ID + ":" + getItemName(planks) + "_from_" + getItemName(wood));
		//@formatter:on
	}

	protected static void stoneFamily(Consumer<FinishedRecipe> gen, StoneFamily family) {
		//@formatter:off
		slab(gen, family.base(), family.slab());
		stair(gen, family.base(), family.stairs());
		pressure_plate(gen, family.base(), family.pressure_plate());
		button(gen, family.base(), family.button());
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.wall(), 6)
			.pattern("BBB")
			.pattern("BBB")
			.define('B', family.base())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(family.wall()));
		
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, family.base(), 1)
			.pattern("PP")
			.pattern("PP")
			.define('P', family.pebbles())
			.unlockedBy(getHasName(family.pebbles()), has(family.pebbles()))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_base_from_" + getItemName(family.pebbles()));
		
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.slab(), 2)
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.slab()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.stairs())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.stairs()));
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(family.base()), RecipeCategory.BUILDING_BLOCKS, family.wall())
			.unlockedBy(getHasName(family.base()), has(family.base()))
			.save(gen, Machina.MOD_ID + ":stonecutting_" + getItemName(family.wall()));
		//@formatter:on
	}

	protected static void stair(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike stair) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stair, 4)
			.pattern("B  ")
			.pattern("BB ")
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(stair));
		//@formatter:on
	}

	protected static void slab(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike slab) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slab, 6)
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(slab));
		//@formatter:on
	}

	protected static void door(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike door) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 3)
			.pattern("BB")
			.pattern("BB")
			.pattern("BB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(door));
		//@formatter:on
	}

	protected static void trapdoor(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike door) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, door, 2)
			.pattern("BBB")
			.pattern("BBB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(door));
		//@formatter:on
	}

	protected static void pressure_plate(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike plate) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, plate)
			.pattern("BB")
			.define('B', base)
			.unlockedBy(getHasName(base), has(base))
			.showNotification(false)
			.save(gen, Machina.MOD_ID + ":crafting_" + getItemName(plate));
		//@formatter:on
	}

	protected static void button(Consumer<FinishedRecipe> gen, ItemLike base, ItemLike button) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, button, 1)
			.requires(base)
			.unlockedBy(getHasName(base), has(base))
			.save(gen, Machina.MOD_ID + ":" + getItemName(button) + "_from_" + getItemName(base));
		//@formatter:on
	}

	protected static void compact(Consumer<FinishedRecipe> gen, ItemLike big, ItemLike small) {
		recompact(gen, big, small);
		decompact(gen, big, small);
	}

	protected static void recompact(Consumer<FinishedRecipe> gen, ItemLike big, ItemLike small) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, big)
	        .pattern("SSS")
	        .pattern("SSS")
	        .pattern("SSS")
	        .define('S', small)
	        .unlockedBy(getHasName(small), has(small))
	        .showNotification(false)
	        .save(gen, Machina.MOD_ID + ":" + getItemName(big) + "_from_" + getItemName(small));
		//@formatter:on
	}

	protected static void decompact(Consumer<FinishedRecipe> gen, ItemLike big, ItemLike small) {
		//@formatter:off
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, small, 9)
	        .requires(big)
	        .unlockedBy(getHasName(big), has(big))
	        .save(gen, Machina.MOD_ID + ":" + getItemName(small) + "_from_" + getItemName(big));
		//@formatter:on
	}

	protected static void ore(Consumer<FinishedRecipe> gen, List<ItemLike> ing, ItemLike res, float exp, int duration,
			String group) {
		oreSmelting(gen, ing, RecipeCategory.MISC, res, exp, duration, group);
		oreBlasting(gen, ing, RecipeCategory.MISC, res, exp, duration / 2, group);
	}

	protected static void oreSmelting(@NotNull Consumer<FinishedRecipe> gen, List<ItemLike> ing,
			@NotNull RecipeCategory cat, @NotNull ItemLike res, float exp, int pCookingTIme, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.SMELTING_RECIPE, ing, cat, res, exp, pCookingTIme, group, "_from_smelting");
	}

	protected static void oreBlasting(@NotNull Consumer<FinishedRecipe> gen, List<ItemLike> ing,
			@NotNull RecipeCategory cat, @NotNull ItemLike res, float exp, int time, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.BLASTING_RECIPE, ing, cat, res, exp, time, group, "_from_blasting");
	}

	protected static void oreCooking(@NotNull Consumer<FinishedRecipe> gen,
			@NotNull RecipeSerializer<? extends AbstractCookingRecipe> ser, List<ItemLike> ing,
			@NotNull RecipeCategory cat, @NotNull ItemLike res, float exp, int time, @NotNull String group,
			String name) {
		for (ItemLike itemlike : ing) {
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), cat, res, exp, time, ser).group(group)
					.unlockedBy(getHasName(itemlike), has(itemlike))
					.save(gen, Machina.MOD_ID + ":" + getItemName(res) + name + "_" + getItemName(itemlike));
		}
	}
}
