package com.machina.capability;

import java.util.LinkedList;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CustomFluidStorage implements IFluidHandler, ICustomStorage {
	
	private final LinkedList<MachinaTank> tanks;

	public CustomFluidStorage(LinkedList<MachinaTank> tanks) {
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
	
	@Nonnull
	public void setFluidInTank(int tank, FluidStack stack) {
		tanks.get(tank).setFluid(stack);
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
	
	public int fillRaw(FluidStack resource, FluidAction action) {
		for (MachinaTank tank : tanks) {
			if (tank.rawFill(resource, FluidAction.SIMULATE) != 0) {
				return tank.rawFill(resource, action);
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



	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		for(MachinaTank tank : tanks) {
			tag.put("fluid_" + tank.id, tank.getFluid().writeToNBT(new CompoundNBT()));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		for(MachinaTank tank : tanks) {
			tank.setFluid(FluidStack.loadFluidStackFromNBT(nbt.getCompound("fluid_" + tank.id)));
		}
	}

	@Override
	public void setChanged(Runnable runnable) {
		for(MachinaTank tank : tanks) {
			tank.onChanged = runnable;
		}
	}

	@Override
	public String getTag() {
		return "fluid";
	}
	
	public MachinaTank tank(int i) {
		return this.tanks.get(i);
	}
}