package com.machina.api.cap.fluid;

import java.util.function.Predicate;

import com.machina.api.network.PacketSender;
import com.machina.api.network.s2c.S2CFluidSync;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MachinaTank extends FluidTank {

	protected Runnable onChanged;

	private final BlockEntity tile;
	public final int id;

	public MachinaTank(BlockEntity tile, int capacity, Predicate<FluidStack> validator, int id) {
		super(capacity, validator);
		this.tile = tile;
		this.id = id;
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

	@Override
	protected void onContentsChanged() {
		if (tile.getLevel() != null && !tile.getLevel().isClientSide()) {
			PacketSender.sendToClients(new S2CFluidSync(tile.getBlockPos(), getFluid(), id));
		}
		onChanged.run();
	}
}
