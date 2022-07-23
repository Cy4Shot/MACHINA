package com.machina.block.container.slot;

import java.util.function.Supplier;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CompletableSlot extends Slot {
	Supplier<ItemStack> accept = () -> ItemStack.EMPTY;
	boolean tag = false;

	public CompletableSlot(IInventory pContainer, int pIndex, int pX, int pY, Supplier<ItemStack> accept,
			boolean checktag) {
		super(pContainer, pIndex, pX, pY);
		this.accept = accept;
		this.tag = checktag;
	}

	public ItemStack getBackground() {
		return accept.get();
	}

	@Override
	public int getMaxStackSize() {
		return accept.get().getCount();
	}
	@Override
	public boolean mayPlace(ItemStack stack) {
		return accept.get().getItem().equals(stack.getItem())
				&& (!tag || accept.get().getOrCreateTag().equals(stack.getOrCreateTag()));
	}

	public boolean isComplete() {
		return getItem().getItem().equals(accept.get().getItem()) && accept.get().getCount() == getItem().getCount();
	}
}