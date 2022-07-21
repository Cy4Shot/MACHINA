package com.machina.util.server;

import javax.annotation.Nonnull;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidUtils {

	public static int canTake(Direction facing, BlockPos pos, World world, int rate) {
		return BlockUtils.getCapability(world, pos.relative(facing), facing.getOpposite(),
				CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(fluidHandler -> {
					FluidStack fluid = new FluidStack(fluidHandler.getFluidInTank(0).getFluid(), rate);
					if (fluid.isEmpty())
						return rate;
					return testTransfer(fluidHandler, fluid).getAmount();
				}).orElse(0);
	}

	public static boolean pushFluidsToTank(Direction facing, BlockPos pos, World world, IFluidHandler tank, int rate) {

		return BlockUtils
				.getCapability(world, pos.relative(facing), facing.getOpposite(),
						CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
				.map(fluidHandler -> !FluidUtil.tryFluidTransfer(fluidHandler, tank, rate, true).isEmpty())
				.orElse(false);
	}

	public static boolean pullFluidsFromTank(Direction facing, BlockPos pos, World world, IFluidHandler tank,
			int rate) {
		return BlockUtils
				.getCapability(world, pos.relative(facing.getOpposite()), facing,
						CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
				.map(fluidHandler -> !FluidUtil.tryFluidTransfer(tank, fluidHandler, rate, true).isEmpty())
				.orElse(false);
	}

	@Nonnull
	private static FluidStack testTransfer(IFluidHandler fluidDestination, FluidStack drainable) {
		int fillableAmount = fluidDestination.fill(drainable, IFluidHandler.FluidAction.SIMULATE);
		if (fillableAmount > 0) {
			drainable.setAmount(fillableAmount);
			return drainable;
		}
		return FluidStack.EMPTY;
	}

}
