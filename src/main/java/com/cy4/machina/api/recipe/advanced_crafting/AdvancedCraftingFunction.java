package com.cy4.machina.api.recipe.advanced_crafting;

import java.util.List;

import com.cy4.machina.compat.jei.category.AdvancedCraftingRecipeExtension;
import com.cy4.machina.recipe.advanced_crafting.function.EmptyFunction;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class AdvancedCraftingFunction {

	/**
	 * Called in {@link AdvancedCraftingRecipe#matches(CraftingInventory, World)}.
	 * Call the super in order to check the recipe pattern
	 * 
	 * @param inv
	 * @param recipe
	 * @return
	 */
	public boolean matches(CraftingInventory inv, AdvancedCraftingRecipe recipe, World level) {
		return recipe.actualMatches(inv, level);
	}

	/**
	 * Called in {@link AdvancedCraftingRecipe#assemble(CraftingInventory)}, in
	 * order to modify the result
	 * 
	 * @param original
	 * @param inv
	 * @param recipe
	 * @return
	 */
	public ItemStack assemble(ItemStack original, CraftingInventory inv, AdvancedCraftingRecipe recipe) {
		return original;
	}

	/**
	 * Called in
	 * {@link AdvancedCraftingRecipeExtension#getTooltipStrings(double, double)} in
	 * order to add function info in the tooltip
	 * 
	 * @param tooltipList
	 */
	public void addJeiInfo(List<ITextComponent> tooltipList) {

	}

	/**
	 * Checks if the
	 * 
	 * @return
	 */
	public final boolean isEmtpy() { return this instanceof EmptyFunction; }

}
