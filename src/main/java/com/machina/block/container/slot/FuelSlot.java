package com.machina.block.container.slot;

import com.machina.util.helper.ItemStackHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FuelSlot extends Slot {

	public FuelSlot(IInventory pContainer, int pIndex, int pX, int pY) {
		super(pContainer, pIndex, pX, pY);
	}

	public boolean mayPlace(ItemStack pStack) {
		return ItemStackHelper.isFuel(pStack) || ItemStackHelper.isBucket(pStack);
	}

	public int getMaxStackSize(ItemStack pStack) {
		return ItemStackHelper.isBucket(pStack) ? 1 : super.getMaxStackSize(pStack);
	}
}