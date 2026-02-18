package com.machina.api.cap.energy;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyBlockItemWrapper extends EnergyItemWrapper {

	public static EnergyBlockItemWrapper from(IEnergyStorage wrapper) {
		if (wrapper instanceof EnergyBlockItemWrapper)
			return (EnergyBlockItemWrapper) wrapper;
		if (wrapper instanceof EnergyItemWrapper)
			return new EnergyBlockItemWrapper(((EnergyItemWrapper) wrapper).container);
		return null;
	}

	public EnergyBlockItemWrapper(@NotNull ItemStack container) {
		super(container);
	}

	@Override
	public boolean canExtract() {
		return false;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}
}
