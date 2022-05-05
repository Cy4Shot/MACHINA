package com.machina.block.container.slot;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CompletableSlot extends Slot {
	Supplier<ItemStack> accept = () -> ItemStack.EMPTY;

	public CompletableSlot(IInventory pContainer, int pIndex, int pX, int pY, Supplier<ItemStack> accept) {
		super(pContainer, pIndex, pX, pY);
		this.accept = accept;
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
		return accept.get().getItem().equals(stack.getItem());
	}

	@Override
	public boolean mayPickup(PlayerEntity pPlayer) {
		return !getItem().getItem().equals(accept.get().getItem());
	}

	public boolean isComplete() {
		return getItem().getItem().equals(accept.get().getItem()) && accept.get().getCount() == getItem().getCount();
	}

}
