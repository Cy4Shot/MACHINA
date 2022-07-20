package com.machina.block.tile.base;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public interface IMultiFluidTileEntity {
	public static final int BUCKET = FluidAttributes.BUCKET_VOLUME;

	public void setFluid(FluidStack fluid, int index);
	
	public FluidStack getFluid(int index);
	
	public int stored(int index);
	
	public int capacity(int index);
	
	public default float propFull(int index) {
		return (float) stored(index) / (float) capacity(index);
	}
}
