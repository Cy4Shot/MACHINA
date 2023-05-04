package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.container.slot.ResultSlot;
import com.machina.block.tile.machine.MixerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

public class MixerContainer extends BaseContainer<MixerTileEntity> {

	public MixerContainer(int id, final PlayerInventory playerInv, MixerTileEntity te) {
		super(ContainerInit.MIXER.get(), id, te);

		recreateSlots(playerInv);
	}

	public void recreateSlots(final PlayerInventory playerInv) {
		this.slots.clear();
		this.addSlot(new Slot((IInventory) te, 0, -6, 139));
		this.addSlot(new Slot((IInventory) te, 1, 80, 95));
		this.addSlot(new ResultSlot((IInventory) te, 2, 164, 139));
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInv, col, 8 + col * 18, 196));
		}
	}

	public MixerContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, inv, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.MIXER.get();
	}
}