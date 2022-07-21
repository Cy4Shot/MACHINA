package com.machina.capability.fluid;

import java.util.function.Predicate;

import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CFluidSync;

import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MachinaTank extends FluidTank {

	private TileEntity tile;
	public int id;
	public boolean output;

	public MachinaTank(TileEntity tile, int capacity, Predicate<FluidStack> validator, boolean output, int id) {
		super(capacity, validator);
		this.tile = tile;
		this.id = id;
		this.output = output;
		this.setFluid(new FluidStack(Fluids.EMPTY, 0));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT nbt) {
		CompoundNBT comp = new CompoundNBT();
		super.writeToNBT(comp);
		nbt.put("MachinaTank" + id, comp);
		return nbt;
	}

	@Override
	public FluidTank readFromNBT(CompoundNBT nbt) {
		return super.readFromNBT(nbt.getCompound("MachinaTank" + id));
	}

	public float propFull() {
		return (float) getFluidAmount() / (float) getCapacity();
	}

	@Override
	public void onContentsChanged() {
		IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
		if (handler == null || getFluid() == null) {
			return;
		}
		if (!tile.getLevel().isClientSide()) {
			MachinaNetwork.sendToClients(MachinaNetwork.CHANNEL, new S2CFluidSync(tile.getBlockPos(), getFluid(), id));
		}
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (!output)
			return FluidStack.EMPTY;
		return super.drain(resource, action);
	}

	public int rawFill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty()) {
			return 0;
		}
		if (action.simulate()) {
			if (fluid.isEmpty()) {
				return Math.min(capacity, resource.getAmount());
			}
			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}
			return Math.min(capacity - fluid.getAmount(), resource.getAmount());
		}
		if (fluid.isEmpty()) {
			fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
			onContentsChanged();
			return fluid.getAmount();
		}
		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - fluid.getAmount();

		if (resource.getAmount() < filled) {
			fluid.grow(resource.getAmount());
			filled = resource.getAmount();
		} else {
			fluid.setAmount(capacity);
		}
		if (filled > 0)
			onContentsChanged();
		return filled;
	}
}