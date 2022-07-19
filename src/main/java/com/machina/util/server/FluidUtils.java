package com.machina.util.server;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class FluidUtils {

	public static boolean hasFluid(IBlockReader world, BlockPos pos, Direction dir) {
		TileEntity te = world.getBlockEntity(pos);
		if (te != null)
			return hasFluid(te, dir);
		return false;
	}

	public static boolean hasFluid(TileEntity te, Direction dir) {
		return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir).isPresent();
	}

	public static boolean tryFill(World world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom,
			int amount) {
		if (amount <= 0)
			return false;
		if (tankFrom == null)
			return false;

		LazyOptional<IFluidHandler> testNull = FluidUtil.getFluidHandler(world, posSide, sideOpp);
		final IFluidHandler fluidTo = testNull == null ? null : testNull.orElse(null);
		if (fluidTo == null)
			return false;
		final FluidStack toBeDrained = tankFrom.drain(amount, FluidAction.SIMULATE);
		if (toBeDrained.isEmpty())
			return false;
		final int filledAmount = fluidTo.fill(toBeDrained, FluidAction.EXECUTE);
		if (filledAmount <= 0)
			return false;
		tankFrom.drain(filledAmount, FluidAction.EXECUTE);
		return true;
	}

}
