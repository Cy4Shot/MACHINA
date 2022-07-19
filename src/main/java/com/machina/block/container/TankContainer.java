package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.TankTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class TankContainer extends BaseContainer<TankTileEntity> {

	public final TankTileEntity te;

	public TankContainer(final int windowId, final TankTileEntity te) {
		super(ContainerInit.TANK.get(), windowId, te);
		this.te = te;
	}

	public TankContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.TANK.get();
	}
}
