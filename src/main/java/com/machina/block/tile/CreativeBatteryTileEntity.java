package com.machina.block.tile;

import com.machina.energy.EnergyDefinition;
import com.machina.registration.init.TileEntityTypesInit;

public class CreativeBatteryTileEntity extends EnergyTileEntity {

	public CreativeBatteryTileEntity() {
		super(TileEntityTypesInit.CREATIVE_BATTERY.get(), new EnergyDefinition(100000, 1000, 1000));
	}

	@Override
	public void tick() {
		fillEnergy();
		transferAll();
	}

}
