package com.cy4.machina.api.recipe.advanced_crafting;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class AdvancedCraftingFunction {

	/**
	 * Called in {@link AdvancedCraftingRecipe#matches(CraftingInventory, World)}. Call the super in order to
	 * check the recipe pattern
	 * 
	 * @param inv
	 * @param recipe
	 * @return
	 */
	public boolean matches(CraftingInventory inv, AdvancedCraftingRecipe recipe, World level) {
		return recipe.actualMatches(inv, level);
	}

	/**
	 * Called in {@link AdvancedCraftingRecipe#assemble(CraftingInventory)}
	 * @param original
	 * @param inv
	 * @param recipe
	 * @return
	 */
	public ItemStack assemble(ItemStack original, CraftingInventory inv, AdvancedCraftingRecipe recipe) {
		return original;
	}

}
