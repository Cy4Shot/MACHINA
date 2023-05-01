package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.machine.StateConverterTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class StateConverterContainer extends BaseContainer<StateConverterTileEntity> {

	public StateConverterContainer(int id, StateConverterTileEntity te) {
		super(ContainerInit.STATE_CONVERTER.get(), id, te);
	}

	public StateConverterContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.STATE_CONVERTER.get();
	}

}
