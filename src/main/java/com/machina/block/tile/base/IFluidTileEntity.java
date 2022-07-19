package com.machina.block.tile.base;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public interface IFluidTileEntity {
	public static final int BUCKET = FluidAttributes.BUCKET_VOLUME;

	public abstract void setFluid(FluidStack fluid);
}
