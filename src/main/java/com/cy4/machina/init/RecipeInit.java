/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
