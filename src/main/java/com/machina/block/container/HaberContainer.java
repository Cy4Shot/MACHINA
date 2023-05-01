package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.AcceptSlot;
import com.machina.block.tile.multiblock.haber.HaberControllerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;
import com.machina.registration.init.ItemInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class HaberContainer extends BaseContainer<HaberControllerTileEntity> {

	public HaberContainer(final int windowId, final PlayerInventory playerInv, final HaberControllerTileEntity te) {
		super(ContainerInit.HABER.get(), windowId, te);

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(
				new AcceptSlot((IInventory) te, 0, -6, 149, i -> i.getItem().equals(ItemInit.IRON_CATALYST.get())));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 196));
		}
	}

	public HaberContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.HABER_CONTROLLER.get();
	}
}
