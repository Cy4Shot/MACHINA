package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.machine.PressurizedChamberTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class PressurizedChamberContainer extends BaseContainer<PressurizedChamberTileEntity> {

	public PressurizedChamberContainer(final int windowId, final PlayerInventory playerInv, final PressurizedChamberTileEntity te) {
		super(ContainerInit.PRESSURIZED_CHAMBER.get(), windowId, te);
		
		recreateSlots(playerInv);
	}
	
	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new Slot((IInventory) te, 0, -6, 149));
		this.addSlot(new ResultSlot((IInventory) te, 1, 108, 149));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 196));
		}
	}

	public PressurizedChamberContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.PRESSURIZED_CHAMBER.get();
	}
}
