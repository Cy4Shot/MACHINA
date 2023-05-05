package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.machine.MelterTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class MelterContainer extends BaseContainer<MelterTileEntity> {

	public MelterContainer(int id, final PlayerInventory playerInv, MelterTileEntity te) {
		super(ContainerInit.MELTER.get(), id, te);

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 196));
		}
	}

	public MelterContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, inv, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.MELTER.get();
	}
}