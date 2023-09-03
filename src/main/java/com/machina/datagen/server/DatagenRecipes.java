package com.machina.datagen.server;

import java.util.List;
import java.util.function.Consumer;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ItemInit;

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

public class DatagenRecipes extends RecipeProvider implements IConditionBuilder {

	public DatagenRecipes(PackOutput po) {
		super(po);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> gen) {
		ore(gen, List.of(ItemInit.RAW_ALUMINUM.get(), BlockInit.ALUMINUM_ORE.get()), ItemInit.ALUMINUM_INGOT.get(),
				0.25f, "aluminum");

		compact(gen, BlockInit.ALUMINUM_BLOCK.get(), ItemInit.ALUMINUM_INGOT.get());
		compact(gen, ItemInit.ALUMINUM_INGOT.get(), ItemInit.ALUMINUM_NUGGET.get());
	}

	protected static void compact(Consumer<FinishedRecipe> gen, ItemLike big, ItemLike small) {
		//@formatter:off
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, big)
	        .pattern("SSS")
	        .pattern("SSS")
	        .pattern("SSS")
	        .define('S', small)
	        .unlockedBy(getHasName(small), has(small))
	        .save(gen, Machina.MOD_ID + ":" + getItemName(big) + "_from_" + getItemName(small));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, small, 9)
	        .requires(big)
	        .unlockedBy(getHasName(big), has(big))
	        .save(gen, Machina.MOD_ID + ":" + getItemName(small) + "_from_" + getItemName(big));
		//@formatter:on
	}

	protected static void ore(Consumer<FinishedRecipe> gen, List<ItemLike> ing, ItemLike res, float exp, String group) {
		oreSmelting(gen, ing, RecipeCategory.MISC, res, exp, 200, group);
		oreBlasting(gen, ing, RecipeCategory.MISC, res, exp, 100, group);
	}

	protected static void oreSmelting(Consumer<FinishedRecipe> gen, List<ItemLike> ing, RecipeCategory cat,
			ItemLike res, float exp, int pCookingTIme, String group) {
		oreCooking(gen, RecipeSerializer.SMELTING_RECIPE, ing, cat, res, exp, pCookingTIme, group, "_from_smelting");
	}

	protected static void oreBlasting(Consumer<FinishedRecipe> gen, List<ItemLike> ing, RecipeCategory cat,
			ItemLike res, float exp, int time, String group) {
		oreCooking(gen, RecipeSerializer.BLASTING_RECIPE, ing, cat, res, exp, time, group, "_from_blasting");
	}

	protected static void oreCooking(Consumer<FinishedRecipe> gen,
			RecipeSerializer<? extends AbstractCookingRecipe> ser, List<ItemLike> ing, RecipeCategory cat, ItemLike res,
			float exp, int time, String group, String name) {
		for (ItemLike itemlike : ing) {
			SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), cat, res, exp, time, ser).group(group)
					.unlockedBy(getHasName(itemlike), has(itemlike))
					.save(gen, Machina.MOD_ID + ":" + getItemName(res) + name + "_" + getItemName(itemlike));
		}
	}
}
