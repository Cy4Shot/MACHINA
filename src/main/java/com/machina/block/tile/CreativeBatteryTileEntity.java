package com.machina.block.tile;

import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.capability.energy.MachinaEnergyStorage;
import com.machina.registration.init.TileEntityTypesInit;

public class CreativeBatteryTileEntity extends BaseEnergyTileEntity {

	public CreativeBatteryTileEntity() {
		super(TileEntityTypesInit.CREATIVE_BATTERY.get());
		
		this.sides = new int[] {2, 2, 2, 2, 2, 2};
	}
	
	@Override
	public MachinaEnergyStorage createStorage() {
		return new MachinaEnergyStorage(this, Integer.MAX_VALUE, 1000, 1000);
	}

	@Override
	public void tick() {
		fillEnergy();
		sendOutPower();
	}

}
