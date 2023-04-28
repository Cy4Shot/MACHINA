package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.BatteryTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class BatteryContainer extends BaseContainer<BatteryTileEntity> {

	public final BatteryTileEntity te;

	public BatteryContainer(final int windowId, final BatteryTileEntity te) {
		super(ContainerInit.BATTERY.get(), windowId, te);
		this.te = te;
	}

	public BatteryContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.BATTERY.get();
	}
}
