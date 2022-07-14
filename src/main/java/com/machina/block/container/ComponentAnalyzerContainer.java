package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class ComponentAnalyzerContainer extends BaseContainer<ComponentAnalyzerTileEntity> {

	public ComponentAnalyzerTileEntity te;

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv,
			final ComponentAnalyzerTileEntity te) {
		super(ContainerInit.COMPONENT_ANALYZER.get(), windowId, te);
		this.te = te;

		recreateSlots(playerInv);

		createData(() -> te.getData());
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new AcceptSlot((IInventory) te, 0, -2, 74, ItemInit.SHIP_COMPONENT.get().getDefaultInstance()));
		this.addSlot(new ResultSlot((IInventory) te, 1, 159, 74));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public ComponentAnalyzerContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected int getContainerSize() {
		return this.te.getContainerSize();
	}

	@Override
	protected Block getBlock() {
		return BlockInit.COMPONENT_ANALYZER.get();
	}
}
