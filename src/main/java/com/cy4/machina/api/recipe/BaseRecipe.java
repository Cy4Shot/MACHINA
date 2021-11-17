package com.cy4.machina.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public abstract class BaseRecipe<I extends IInventory> implements IRecipe<I> {
	
	protected ItemStack result = ItemStack.EMPTY;

	@Override
	public boolean isSpecial() { return true; }

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return false;
	}

	@Override
	public ItemStack getResultItem() { return result; }
}
