package com.machina.block.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ResultSlot extends Slot {

	public ResultSlot(IInventory pContainer, int pIndex, int pX, int pY) {
		super(pContainer, pIndex, pX, pY);
	}
	
	@Override
	public boolean mayPlace(ItemStack pStack) {
		return false;
	}
}