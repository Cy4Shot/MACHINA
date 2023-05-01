package com.machina.block.container.base;

import java.util.Objects;
import java.util.function.Supplier;

import com.machina.block.tile.MachinaTileEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

public abstract class BaseContainer<T extends TileEntity> extends Container {

	protected final IWorldPosCallable canInteractWithCallable;
	public IIntArray data;
	
	public T te;

	public BaseContainer(ContainerType<?> type, int id, T te) {
		super(type, id);
		this.canInteractWithCallable = IWorldPosCallable.create(te.getLevel(), te.getBlockPos());
		this.te = te;
	}

	@SuppressWarnings("unchecked")
	protected static <T extends TileEntity> T getTileEntity(final PlayerInventory playerInv, final PacketBuffer data) {
		Objects.requireNonNull(playerInv, "Player Inventory cannot be null.");
		Objects.requireNonNull(data, "Packet Buffer cannot be null.");
		final TileEntity te = playerInv.player.level.getBlockEntity(data.readBlockPos());
		return (T) te;
	}

	protected void createData(Supplier<IIntArray> s) {
		this.data = s.get();
		checkContainerDataCount(data, data.getCount());
		this.addDataSlots(data);
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, getBlock());
	}

	protected abstract Block getBlock();

	protected int getContainerSize() {
		if (te.getClass().isAssignableFrom(MachinaTileEntity.class)) {
			return ((MachinaTileEntity) te).getContainerSize();
		}
		return 0;
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack stack1 = slot.getItem();
			stack = stack1.copy();
			if (index < getContainerSize()
					&& !this.moveItemStackTo(stack1, getContainerSize(), this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
			if (!this.moveItemStackTo(stack1, 0, getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return stack;
	}

}
