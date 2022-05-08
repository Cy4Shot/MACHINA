package com.machina.block.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AcceptSlot extends Slot {
	
	Item item;

	public AcceptSlot(IInventory pContainer, int pIndex, int pX, int pY, Item item) {
		super(pContainer, pIndex, pX, pY);
		this.item = item;
	}
	
	@Override
	public boolean mayPlace(ItemStack pStack) {
		return pStack.getItem().equals(this.item);
	}

}
