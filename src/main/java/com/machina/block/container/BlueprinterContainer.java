package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.machine.BlueprinterTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class BlueprinterContainer extends BaseContainer<BlueprinterTileEntity> {

	public BlueprinterTileEntity te;

	public BlueprinterContainer(final int windowId, final PlayerInventory playerInv, final BlueprinterTileEntity te) {
		super(ContainerInit.BLUEPRINTER.get(), windowId, te);
		this.te = te;

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(
				new AcceptSlot((IInventory) te, 0, 118, 33, stack -> stack.getItem().equals(ItemInit.BLUEPRINT.get())));
		this.addSlot(new ResultSlot((IInventory) te, 1, 165, 33));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 7 + col * 18, 176));
		}
	}

	public BlueprinterContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected int getContainerSize() {
		return this.te.getContainerSize();
	}

	@Override
	protected Block getBlock() {
		return BlockInit.BLUEPRINTER.get();
	}
}
