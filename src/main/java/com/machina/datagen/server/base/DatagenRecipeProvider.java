package com.machina.datagen.server.base;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.machina.Machina;
import com.machina.api.recipe.MachinaRecipeBuilder;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.registration.init.RecipeInit;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;

public abstract class DatagenRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public DatagenRecipeProvider(PackOutput po) {
		super(po);
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
		oreSmelting(gen, ing, res, exp, duration, group);
		oreBlasting(gen, ing, res, exp, duration / 2, group);
	}

	protected static void oreSmelting(@NotNull Consumer<FinishedRecipe> gen, List<ItemLike> ing, @NotNull ItemLike res,
			float exp, int pCookingTIme, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.SMELTING_RECIPE, ing, res, exp, pCookingTIme, group, "_from_smelting");
	}

	protected static void oreBlasting(@NotNull Consumer<FinishedRecipe> gen, List<ItemLike> ing, @NotNull ItemLike res,
			float exp, int time, @NotNull String group) {
		oreCooking(gen, RecipeSerializer.BLASTING_RECIPE, ing, res, exp, time, group, "_from_blasting");
	}

	protected static void oreCooking(@NotNull Consumer<FinishedRecipe> gen,
			@NotNull RecipeSerializer<? extends AbstractCookingRecipe> ser, List<ItemLike> ing, @NotNull ItemLike res,
			float exp, int time, @NotNull String group, String name) {
		for (ItemLike itemlike : ing) {
			//@formatter:off
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), RecipeCategory.MISC, res, exp, time, ser)
					.group(group)
					.unlockedBy(getHasName(itemlike), has(itemlike))
					.save(gen, Machina.MOD_ID + ":" + getItemName(res) + name + "_" + getItemName(itemlike));
			//@formatter:on
		}
	}

	// Machina Recipe Builders
	protected static void mixing(@NotNull Consumer<FinishedRecipe> gen, FluidObject input1, int amount1,
			FluidObject input2, int amount2, FluidObject output, int amount, int energy) {
		//@formatter:off
		MachinaRecipeBuilder.create(RecipeInit.MIXER)
			.withInputFluid(new FluidStack(input1.fluid(), amount1))
			.withInputFluid(new FluidStack(input2.fluid(), amount2))
			.withOutputFluid(new FluidStack(output.fluid(), amount))
			.withEnergy(energy)
			.save(gen, "mixing_" + output.name());
		//@formatter:on
	}
}
