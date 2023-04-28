package com.machina.block.tile;

import com.machina.block.tile.base.CustomTE;
import com.machina.capability.CustomEnergyStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.EnergyHelper;

import net.minecraft.tileentity.ITickableTileEntity;

public class CreativeBatteryTileEntity extends CustomTE implements ITickableTileEntity {

	public CreativeBatteryTileEntity() {
		super(TileEntityInit.CREATIVE_BATTERY.get());
	}

	CustomEnergyStorage energy;

	@Override
	public void createStorages() {
		add(new CustomEnergyStorage(10000, 1000));
	}

	@Override
	public void tick() {
		if (level.isClientSide())
			return;
		this.energy.addEnergy(this.energy.getMaxEnergyStored() - this.energy.getEnergyStored());
		EnergyHelper.sendOutPower(energy, level, worldPosition, () -> setChanged());
	}
}