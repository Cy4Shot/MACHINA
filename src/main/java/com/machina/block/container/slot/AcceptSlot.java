package com.machina.block.container.slot;

import java.util.function.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AcceptSlot extends Slot {
	
	Predicate<ItemStack> item;

	public AcceptSlot(IInventory pContainer, int pIndex, int pX, int pY, Predicate<ItemStack> stack) {
		super(pContainer, pIndex, pX, pY);
		this.item = stack;
	}
	
	@Override
	public boolean mayPlace(ItemStack pStack) {
		return item.test(pStack);
	}
}