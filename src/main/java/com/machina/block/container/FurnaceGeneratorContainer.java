package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.FuelSlot;
import com.machina.block.tile.FurnaceGeneratorTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class FurnaceGeneratorContainer extends BaseContainer<FurnaceGeneratorTileEntity> {

	public final FurnaceGeneratorTileEntity te;

	public FurnaceGeneratorContainer(final int windowId, final PlayerInventory inv,
			final FurnaceGeneratorTileEntity te) {
		super(ContainerInit.FURNACE_GENERATOR.get(), windowId, te);
		this.te = te;

		recreateSlots(inv);

		createData(() -> te.getData());
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new FuelSlot((IInventory) te, 0, 79, 85));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
		}
	}

	public FurnaceGeneratorContainer(final int windowId, final PlayerInventory inv, final PacketBuffer data) {
		this(windowId, inv, getTileEntity(inv, data));
	}

	@Override
	protected int getContainerSize() {
		return 1;
	}

	@Override
	protected Block getBlock() {
		return BlockInit.FURNACE_GENERATOR.get();
	}
}
