package com.machina.api.cap.energy;

import org.jetbrains.annotations.NotNull;

import com.machina.api.item.EnergyItem;
import com.machina.registration.init.DataComponentsInit;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyItemWrapper implements IEnergyStorage {

	@NotNull
	protected final ItemStack container;

	public EnergyItemWrapper(@NotNull ItemStack container) {
		this.container = container;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		int energy = getEnergyStored();
		int received = Math.min(getMaxEnergyStored() - getEnergyStored(), maxReceive);
		if (received > 0 && !simulate) {
			setEnergyStored(energy + received);
		}
		return received;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int energy = getEnergyStored();
		int extracted = Math.min(energy, maxExtract);
		if (extracted > 0 && !simulate) {
			setEnergyStored(energy - extracted);
		}
		return extracted;
	}

	@Override
	public int getEnergyStored() {
        Integer energy = this.container.get(DataComponentsInit.ENERGY);
		return energy == null ? 0 : energy;
	}

	public void setEnergyStored(int energy) {
		this.container.set(DataComponentsInit.ENERGY, energy);
	}

	@Override
	public int getMaxEnergyStored() {
		Item item = this.container.getItem();
		if (item instanceof EnergyItem) {
			return ((EnergyItem) item).getMaxEnergy();
		}
		return 0;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}
}
