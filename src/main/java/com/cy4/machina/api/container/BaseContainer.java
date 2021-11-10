package com.cy4.machina.api.container;

import com.cy4.machina.api.tile_entity.BaseTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseContainer<T extends BaseTileEntity> extends Container {
	
	public final T tile;
	/**
	 * Define this in the constructor. Will be used by {@link #quickMoveStack}
	 */
	public int slotsNumber;
	/**
	 * Define this in the constructor. Will be used to render the inventory name
	 */
	public ITextComponent containerName;
	
	protected BaseContainer(ContainerType<?> pMenuType, int pContainerId, T tile) {
		super(pMenuType, pContainerId);
		this.tile = tile;
	}
	
	/**
	 * Handles shift-clicking in the inventory, based on {@link #slotsNumber}
	 */
	@Override
	public ItemStack quickMoveStack(PlayerEntity pPlayer, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (!slot.isActive())
			return ItemStack.EMPTY;
		if (slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if ((index < slotsNumber && !this.moveItemStackTo(stack1, slotsNumber, this.slots.size(), true)) || !this.moveItemStackTo(stack1, 0, slotsNumber, false))
				return ItemStack.EMPTY;

			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return stack;
	}

}
