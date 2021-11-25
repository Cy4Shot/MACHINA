/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.tile_entity.util;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.energy.EnergyStorage;

public class MachinaEnergyStorage extends EnergyStorage {

	public MachinaEnergyStorage(int capacity) {
		super(capacity);
	}

	public MachinaEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public MachinaEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public MachinaEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		setChanged();
		return super.receiveEnergy(maxReceive, simulate);
	}

	public int receiveInternalEnergy(int maxReceive, boolean simulate) {
		int energyReceived = Math.min(capacity - energy, maxReceive);
		if (!simulate) {
			energy += energyReceived;
		}
		setChanged();
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		setChanged();
		return super.extractEnergy(maxExtract, simulate);
	}

	public void setChanged() {
	}

	public CompoundNBT serialize() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("storedEnergy", energy);
		return nbt;
	}

	public void deserialize(CompoundNBT nbt) {
		energy = nbt.getInt("storedEnergy");
		setChanged();
	}

}
