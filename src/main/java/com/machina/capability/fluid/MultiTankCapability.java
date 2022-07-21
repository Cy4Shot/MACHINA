package com.machina.capability.fluid;

import java.util.LinkedList;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class MultiTankCapability implements IFluidHandler {

	private final LinkedList<MachinaTank> tanks;

	public MultiTankCapability(LinkedList<MachinaTank> tanks) {
		this.tanks = tanks;
	}

	public boolean isEmpty() {
		return tanks.isEmpty();
	}

	@Override
	public int getTanks() {
		return tanks.size();
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank) {
		return tanks.get(tank).getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return tanks.get(tank).getTankCapacity(tank);
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
		return tanks.get(tank).isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		for (MachinaTank tank : tanks) {
			if (tank.fill(resource, FluidAction.SIMULATE) != 0) {
				return tank.fill(resource, action);
			}
		}
		return 0;
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		for (MachinaTank tank : tanks) {
			if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
				return tank.drain(resource, action);
			}
		}
		return FluidStack.EMPTY;
	}
	
	@Nonnull
	public FluidStack drainRaw(FluidStack resource, FluidAction action) {
		for (MachinaTank tank : tanks) {
			if (!tank.drainRaw(resource, FluidAction.SIMULATE).isEmpty()) {
				return tank.drainRaw(resource, action);
			}
		}
		return FluidStack.EMPTY;
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		for (MachinaTank tank : tanks) {
			if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
				return tank.drain(maxDrain, action);
			}
		}
		return FluidStack.EMPTY;
	}
}