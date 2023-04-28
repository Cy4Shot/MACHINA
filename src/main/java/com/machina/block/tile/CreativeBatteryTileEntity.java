package com.machina.block.tile;

import com.machina.block.tile.base.MachinaTileEntity;
import com.machina.capability.energy.IEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.server.EnergyHelper;

import net.minecraft.tileentity.ITickableTileEntity;

public class CreativeBatteryTileEntity extends MachinaTileEntity implements ITickableTileEntity, IEnergyTileEntity {

	public CreativeBatteryTileEntity() {
		super(TileEntityInit.CREATIVE_BATTERY.get());
	}

	MachinaEnergyStorage energy;

	@Override
	public void createStorages() {
		this.energy = add(new MachinaEnergyStorage(this, 10000, 1000));
	}

	@Override
	public void tick() {
		if (level.isClientSide())
			return;
		this.energy.addEnergy(this.energy.getMaxEnergyStored() - this.energy.getEnergyStored());
		EnergyHelper.sendOutPower(energy, level, worldPosition, () -> setChanged());
	}

	@Override
	public boolean isGeneratorMode() {
		return true;
	}
}