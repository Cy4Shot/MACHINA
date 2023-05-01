package com.machina.block.container;

import com.machina.block.container.base.BaseContainer;
import com.machina.block.tile.machine.TemperatureRegulatorTileEntity;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.ContainerInit;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class TemperatureRegulatorContainer extends BaseContainer<TemperatureRegulatorTileEntity> {

	public TemperatureRegulatorContainer(int id, TemperatureRegulatorTileEntity te) {
		super(ContainerInit.TEMPERATURE_REGULATOR.get(), id, te);
	}

	public TemperatureRegulatorContainer(final int id, final PlayerInventory inv, final PacketBuffer data) {
		this(id, getTileEntity(inv, data));
	}

	@Override
	protected Block getBlock() {
		return BlockInit.TEMPERATURE_REGULATOR.get();
	}
}