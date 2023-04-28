package com.machina.capability.heat;

import net.minecraft.tileentity.ITickableTileEntity;

public interface IHeatTileEntity extends ITickableTileEntity {
	
	public float getHeat();
	
	public boolean isGenerator();

}
