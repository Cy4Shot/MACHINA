/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.tile_entity;

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

	public int getMaxExtract() { return this.maxExtract; }
	public int getMaxReceive() { return this.maxReceive; }
}
