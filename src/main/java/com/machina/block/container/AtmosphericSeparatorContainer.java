package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.AtmosphericSeparatorTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class AtmosphericSeparatorContainer extends BaseContainer<AtmosphericSeparatorTileEntity> {

	public final AtmosphericSeparatorTileEntity te;

	public AtmosphericSeparatorContainer(int id, AtmosphericSeparatorTileEntity te) {
		super(ContainerInit.ATMOSPHERIC_SEPARATOR.get(), id, te);
		this.te = te;

		createData(() -> te.getData());
	}

	public AtmosphericSeparatorContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.ATMOSPHERIC_SEPARATOR.get();
	}
}