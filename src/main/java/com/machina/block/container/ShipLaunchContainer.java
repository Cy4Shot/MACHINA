package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class ShipLaunchContainer extends BaseContainer<ShipConsoleTileEntity> {

	public final ShipConsoleTileEntity te;

	public ShipLaunchContainer(final int windowId, final PlayerInventory playerInv, final ShipConsoleTileEntity te) {
		super(ContainerInit.SHIP_LAUNCH.get(), windowId, te);
		this.te = te;
		createData(() -> te.getData());
	}

	public ShipLaunchContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.SHIP_CONSOLE.get();
	}

	@Override
	protected int getContainerSize() {
		return 0;
	}
}
