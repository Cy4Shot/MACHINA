package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.CompletableSlot;
import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class ShipConstructContainer extends BaseContainer<ShipConsoleTileEntity> {

	public final ShipConsoleTileEntity te;

	public ShipConstructContainer(final int windowId, final PlayerInventory playerInv, final ShipConsoleTileEntity te) {
		super(ContainerInit.SHIP_CONSTRUCT.get(), windowId, te);
		this.te = te;
		recreateSlots(playerInv);

		createData(() -> te.getData());
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new CompletableSlot((IInventory) te, 0, -6, 60, () -> te.getItemForStage(0), true));
		this.addSlot(new CompletableSlot((IInventory) te, 1, 19, 60, () -> te.getItemForStage(1), false));
		this.addSlot(new CompletableSlot((IInventory) te, 2, -6, 85, () -> te.getItemForStage(2), false));
		this.addSlot(new CompletableSlot((IInventory) te, 3, 19, 85, () -> te.getItemForStage(3), false));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public ShipConstructContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.SHIP_CONSOLE.get();
	}

	public CompletableSlot getCompletableSlot(int pSlotId) {
		return (CompletableSlot) super.getSlot(pSlotId);
	}

	public boolean areSlotsComplete() {
		return this.getCompletableSlot(0).isComplete() && this.getCompletableSlot(1).isComplete()
				&& this.getCompletableSlot(2).isComplete() && this.getCompletableSlot(3).isComplete();
	}
	
	@Override
	protected int getContainerSize() {
		return this.te.getContainerSize();
	}
}
