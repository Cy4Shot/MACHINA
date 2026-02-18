package com.machina.api.cap.energy;

import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.api.cap.IMachinaStorage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.IEnergyStorage;

/**
 * Block entity storage for energy.
 *
 * @author Cy4
 * @since Machina v0.1.0
 */
public class MachinaEnergyStorage implements IEnergyStorage, IMachinaStorage {

	private final Direction facing;
	private final MachinaBlockEntity be;

	public MachinaEnergyStorage(Direction facing, MachinaBlockEntity be) {
		this.facing = facing;
		this.be = be;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return be.receiveEnergy(facing, maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return be.getEnergy();
	}

	@Override
	public int getMaxEnergyStored() {
		return be.getMaxEnergy();
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public CompoundTag serialize() {
		return new CompoundTag();
	}

	@Override
	public void deserialize(CompoundTag nbt) {

	}
}