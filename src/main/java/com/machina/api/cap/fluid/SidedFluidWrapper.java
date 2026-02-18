package com.machina.api.cap.fluid;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.machina.api.block.entity.MachinaBlockEntity;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class SidedFluidWrapper implements IFluidHandler {
	protected final MachinaBlockEntity inv;
	@Nullable
	protected final Direction side;

	public static IFluidHandler[] create(MachinaBlockEntity inv, Direction... sides) {
		IFluidHandler[] ret = new IFluidHandler[sides.length];
		for (int x = 0; x < sides.length; x++) {
			final Direction side = sides[x];
			ret[x] = new SidedFluidWrapper(inv, side);
		}
		return ret;
	}

	public SidedFluidWrapper(MachinaBlockEntity inv, @Nullable Direction side) {
		this.inv = inv;
		this.side = side;
	}

	@Override
	public int getTanks() {
		return inv.getTanks();
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return inv.getTank(tank).getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return inv.getTank(tank).getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return inv.getTank(tank).isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return inv.fill(side, resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return inv.drain(side, resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return inv.drain(side, maxDrain, action);
	}

}
