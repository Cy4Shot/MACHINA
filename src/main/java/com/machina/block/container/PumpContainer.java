package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.multiblock.pump.PumpControllerTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class PumpContainer extends BaseContainer<PumpControllerTileEntity> {

	public PumpContainer(final int windowId, final PlayerInventory playerInv, final PumpControllerTileEntity te) {
		super(ContainerInit.PUMP.get(), windowId, te);
	}

	public PumpContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, playerInv, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.PUMP_CONTROLLER.get();
	}
}
