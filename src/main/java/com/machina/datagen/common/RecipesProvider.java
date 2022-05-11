package com.machina.datagen.common;

import java.util.function.Consumer;

import com.machina.datagen.builder.CustomSmithingRecipeBuilder;
import com.machina.registration.init.ItemInit;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public class RecipesProvider extends RecipeProvider {

	public RecipesProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> finished) {
		addSmithing(Items.STICK, Items.IRON_BLOCK, ItemInit.REINFORCED_STICK.get(), finished);
	}

	public void addSmithing(Item base, Item add, Item out, Consumer<IFinishedRecipe> finished) {
		CustomSmithingRecipeBuilder.smithing(Ingredient.of(base), Ingredient.of(add), out)
				.unlocks("has_" + base.getRegistryName().getPath(), has(base))
				.save(finished, out.getRegistryName().getPath() + "_smithing");
		;
	}

}
