package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.AtmosphericSeperatorTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class AtmosphericSeperatorContainer extends BaseContainer<AtmosphericSeperatorTileEntity> {

	public final AtmosphericSeperatorTileEntity te;

	public AtmosphericSeperatorContainer(int id, AtmosphericSeperatorTileEntity te) {
		super(ContainerInit.ATMOSPHERIC_SEPERATOR.get(), id, te);
		this.te = te;

		createData(() -> te.getData());
	}
	
	public AtmosphericSeperatorContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.ATMOSPHERIC_SEPERATOR.get();
	}

}
