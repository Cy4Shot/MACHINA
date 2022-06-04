package com.machina.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyDefinition extends EnergyStorage {

	private final int input, output;

	public EnergyDefinition(int capacity, int input, int output) {
		super(capacity);
		this.input = input;
		this.output = output;
	}

	public int getInput() {
		return input;
	}

	public int getOutput() {
		return output;
	}

	public void save(CompoundNBT nbt) {
		nbt.putInt("Energy", energy);
	}

	public void load(CompoundNBT nbt) {
		this.energy = nbt.getInt("Energy");
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (super.energy + maxReceive > super.capacity)
			return super.receiveEnergy(capacity - energy, simulate);
		return super.receiveEnergy(maxReceive, simulate);
	}

}
