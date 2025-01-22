package com.machina.api.cap.fluid;

import javax.annotation.Nonnull;

import com.machina.api.cap.IMachinaStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public record MachinaFluidStorage(MachinaTank tank)
		implements IFluidHandler, IMachinaStorage, INBTSerializable<CompoundTag> {

	@Override
	public int getTanks() {
		return 1;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int id) {
		return tank.getFluid();
	}

	public void setFluidInTank(int id, FluidStack stack) {
		tank.setFluid(stack);
	}

	@Override
	public int getTankCapacity(int id) {
		return tank.getTankCapacity(id);
	}

	@Override
	public boolean isFluidValid(int id, @Nonnull FluidStack stack) {
		return tank.isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (tank.fill(resource, FluidAction.SIMULATE) != 0) {
			return tank.fill(resource, action);
		}
		return 0;
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
			return tank.drain(resource, action);
		}
		return FluidStack.EMPTY;
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
			return tank.drain(maxDrain, action);
		}
		return FluidStack.EMPTY;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.put("fluid_" + tank.id, tank.getFluid().writeToNBT(new CompoundTag()));
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		tank.setFluid(FluidStack.loadFluidStackFromNBT(nbt.getCompound("fluid_" + tank.id)));
	}

	@Override
	public CompoundTag serialize() {
		return this.serializeNBT();
	}

	@Override
	public void deserialize(CompoundTag nbt) {
		this.deserializeNBT(nbt);
	}
}
