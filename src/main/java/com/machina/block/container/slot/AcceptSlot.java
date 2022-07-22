package com.machina.block.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AcceptSlot extends Slot {
	
	ItemStack item;

	public AcceptSlot(IInventory pContainer, int pIndex, int pX, int pY, ItemStack item) {
		super(pContainer, pIndex, pX, pY);
		this.item = item;
	}
	
	@Override
	public boolean mayPlace(ItemStack pStack) {
		return pStack.getItem().equals(this.item.getItem()) && this.item.getTag().equals(pStack.getTag());
	}
}