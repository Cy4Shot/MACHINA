package com.machina.block.tile.base;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidTileEntity {
	public static final int BUCKET = FluidAttributes.BUCKET_VOLUME;

	public void setFluid(FluidStack fluid);
	
	public FluidStack getFluid();
	
	public int stored();
	
	public int capacity();
	
	public default float propFull() {
		return (float) stored() / (float) capacity();
	}
}
