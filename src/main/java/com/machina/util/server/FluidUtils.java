package com.machina.util.server;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidUtils {

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
}