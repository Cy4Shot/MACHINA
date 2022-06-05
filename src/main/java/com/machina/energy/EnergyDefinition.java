package com.machina.energy;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
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

	public static boolean hasEnergy(@Nullable TileEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(CapabilityEnergy.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}

}
