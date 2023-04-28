package com.machina.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements ICustomStorage {
	
	private Runnable onChanged;

	public CustomEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	protected void onEnergyChanged() {
		onChanged.run();
	}

	public void setEnergy(int energy) {
		this.energy = energy;
		onEnergyChanged();
	}

	public void addEnergy(int energy) {
		this.energy += energy;
		if (this.energy > getMaxEnergyStored()) {
			this.energy = getEnergyStored();
		}
		onEnergyChanged();
	}

	public void consumeEnergy(int energy) {
		this.energy -= energy;
		if (this.energy < 0) {
			this.energy = 0;
		}
		onEnergyChanged();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setEnergy(nbt.getInt("energy"));
	}

	@Override
	public void setChanged(Runnable runnable) {
		this.onChanged = runnable;
	}

	@Override
	public String getTag() {
		return "energy";
	}
	
	public boolean isFull() {
		return this.capacity == this.energy;
	}
	
	public static boolean hasEnergy(@Nullable TileEntity te, @Nullable Direction dir) {
		return (te == null ? LazyOptional.empty()
				: te.getCapability(CapabilityEnergy.ENERGY, dir != null ? dir.getOpposite() : null)).isPresent();
	}
}