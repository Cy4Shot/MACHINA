package com.machina.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IMachinaRecipe extends IRecipe<IInventory> {
	@Override
	default boolean isSpecial() {
		return true;
	}
	
	@Override
	default boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	default ItemStack assemble(IInventory pInv) {
		return ItemStack.EMPTY;
	}
}
