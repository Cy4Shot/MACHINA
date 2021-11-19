package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.annotation.registries.recipe.RegisterRecipeType;
import com.cy4.machina.api.recipe.advanced_crafting.AdvancedCraftingRecipe;

import net.minecraft.item.crafting.IRecipeType;

@RegistryHolder
public final class RecipeInit {

	@RegisterRecipeType("advanced_crafting")
    public static final IRecipeType<AdvancedCraftingRecipe> ADVANCED_CRAFTING_RECIPE_TYPE = new IRecipeType<AdvancedCraftingRecipe>() {};
	
}
