package com.machina.block.tile;

import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.energy.EnergyDefinition;
import com.machina.registration.init.TileEntityTypesInit;

public class CreativeBatteryTileEntity extends BaseEnergyTileEntity {

	public CreativeBatteryTileEntity() {
		super(TileEntityTypesInit.CREATIVE_BATTERY.get(), new EnergyDefinition(100000, 1000, 1000));
		
		this.sides = new int[] {2, 2, 2, 2, 2, 2};
	}

	@Override
	public void tick() {
		fillEnergy();
		super.tick();
	}

}
