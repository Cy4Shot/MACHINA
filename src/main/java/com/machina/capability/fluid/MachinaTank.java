package com.machina.capability.fluid;

import java.util.function.Predicate;

import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CFluidSync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class MachinaTank extends FluidTank {

	protected Runnable onChanged;

	private final BlockEntity tile;
	public int id;
	public boolean output;

	public MachinaTank(BlockEntity tile, int capacity, Predicate<FluidStack> validator, boolean output, int id) {
		super(capacity, validator);
		this.tile = tile;
		this.id = id;
		this.output = output;
		this.setFluid(new FluidStack(Fluids.EMPTY, 0));
	}

	@Override
	public CompoundTag writeToNBT(CompoundTag nbt) {
		CompoundTag comp = new CompoundTag();
		super.writeToNBT(comp);
		nbt.put("MachinaTank" + id, comp);
		return nbt;
	}

	@Override
	public FluidTank readFromNBT(CompoundTag nbt) {
		return super.readFromNBT(nbt.getCompound("MachinaTank" + id));
	}

	public float propFull() {
		return (float) getFluidAmount() / (float) getCapacity();
	}

	public boolean isFull() {
		return getSpace() == 0;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		if (!output)
			return FluidStack.EMPTY;
		return super.drain(resource, action);
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		if (!output)
			return FluidStack.EMPTY;
		return super.drain(maxDrain, action);
	}

	public FluidStack drainRaw(FluidStack resource, FluidAction action) {
		if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
			return FluidStack.EMPTY;
		}
		return super.drain(resource.getAmount(), action);
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

	@Override
	protected void onContentsChanged() {
		if (tile.getLevel() != null && !tile.getLevel().isClientSide()) {
			MachinaNetwork.sendToClients(new S2CFluidSync(tile.getBlockPos(), getFluid(), id));
		}
		onChanged.run();
	}
}
