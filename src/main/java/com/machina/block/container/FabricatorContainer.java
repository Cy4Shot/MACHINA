package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.FabricatorTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class FabricatorContainer extends BaseContainer<FabricatorTileEntity> {

	public FabricatorTileEntity te;

	public FabricatorContainer(final int windowId, final PlayerInventory playerInv, final FabricatorTileEntity te) {
		super(ContainerInit.BLUEPRINTER.get(), windowId, te);
		this.te = te;

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 7 + col * 18, 176));
		}
	}

	public FabricatorContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected int getContainerSize() {
		return this.te.getContainerSize();
	}

	@Override
	protected Block getBlock() {
		return BlockInit.FABRICATOR.get();
	}
}
