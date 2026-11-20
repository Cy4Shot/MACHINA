package com.machina.api.block.menu.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.machina.block.entity.machine.steam_turbine.SteamTurbineControllerBlockEntity;

import net.minecraft.world.inventory.ContainerData;

public class SteamTurbinePropData implements ContainerData {

	public static final SteamTurbinePropData of(
			Supplier<SteamTurbineControllerBlockEntity> data,
			Consumer<Integer> lastSteamRate,
			Consumer<Integer> lastEnergyProduced,
			Consumer<Integer> efficiencyScaled,
			Consumer<Integer> bufferEfficiencyScaled,
			Consumer<Integer> energy,
			Consumer<Integer> maxEnergy) {
		return new SteamTurbinePropData(data, lastSteamRate, lastEnergyProduced, efficiencyScaled, bufferEfficiencyScaled, energy, maxEnergy);
	}

	private final Supplier<SteamTurbineControllerBlockEntity> data;
	private final Consumer<Integer> lastSteamRate;
	private final Consumer<Integer> lastEnergyProduced;
	private final Consumer<Integer> efficiencyScaled;
	private final Consumer<Integer> bufferEfficiencyScaled;
	private final Consumer<Integer> energy;
	private final Consumer<Integer> maxEnergy;

	private SteamTurbinePropData(
			Supplier<SteamTurbineControllerBlockEntity> data,
			Consumer<Integer> lastSteamRate,
			Consumer<Integer> lastEnergyProduced,
			Consumer<Integer> efficiencyScaled,
			Consumer<Integer> bufferEfficiencyScaled,
			Consumer<Integer> energy,
			Consumer<Integer> maxEnergy) {
		this.data = data;
		this.lastSteamRate = lastSteamRate;
		this.lastEnergyProduced = lastEnergyProduced;
		this.efficiencyScaled = efficiencyScaled;
		this.bufferEfficiencyScaled = bufferEfficiencyScaled;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
	}

	private SteamTurbineControllerBlockEntity be() {
		return data.get();
	}

	@Override
	public int get(int index) {
		SteamTurbineControllerBlockEntity be = be();
		if (be == null) return 0;
		return switch (index) {
			case 0 -> be.getLastSteamRate();
			case 1 -> be.getLastEnergyProduced();
			case 2 -> (int) (be.getEfficiency() * 10000);
			case 3 -> (int) (be.getBufferEfficiency() * 10000);
			case 4 -> be.getEnergy();
			case 5 -> be.getMaxEnergy();
			default -> throw new IllegalArgumentException("Illegal SteamTurbinePropData index: " + index);
		};
	}

	@Override
	public void set(int index, int value) {
		switch (index) {
			case 0 -> lastSteamRate.accept(value);
			case 1 -> lastEnergyProduced.accept(value);
			case 2 -> efficiencyScaled.accept(value);
			case 3 -> bufferEfficiencyScaled.accept(value);
			case 4 -> energy.accept(value);
			case 5 -> maxEnergy.accept(value);
			default -> throw new IllegalArgumentException("Illegal SteamTurbinePropData index: " + index);
		}
	}

	@Override
	public int getCount() {
		return 6;
	}
}
