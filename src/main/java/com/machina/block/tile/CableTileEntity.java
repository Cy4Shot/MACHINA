package com.machina.block.tile;

import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.energy.EnergyDefinition;
import com.machina.registration.init.TileEntityTypesInit;

import net.minecraft.tileentity.TileEntityType;

public class CableTileEntity extends BaseEnergyTileEntity {

	public CableTileEntity(TileEntityType<?> type) {
		super(type, new EnergyDefinition(1000, 1000, 1000));

		sides = new int[] { 3, 3, 3, 3, 3, 3 };
	}

	public CableTileEntity() {
		this(TileEntityTypesInit.CABLE.get());
	}
}
