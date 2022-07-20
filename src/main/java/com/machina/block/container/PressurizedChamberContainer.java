package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.PressurizedChamberTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class PressurizedChamberContainer extends BaseContainer<PressurizedChamberTileEntity> {

	public final PressurizedChamberTileEntity te;

	public PressurizedChamberContainer(final int windowId, final PressurizedChamberTileEntity te) {
		super(ContainerInit.PRESSURIZED_CHAMBER.get(), windowId, te);
		this.te = te;

		createData(() -> te.getData());
	}

	public PressurizedChamberContainer(final int windowId, final PlayerInventory playerInv, final PacketBuffer data) {
		this(windowId, getTileEntity(playerInv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.PRESSURIZED_CHAMBER.get();
	}
}
