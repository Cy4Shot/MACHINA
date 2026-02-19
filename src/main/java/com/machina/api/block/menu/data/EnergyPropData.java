package com.machina.api.block.menu.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.machina.api.block.entity.MachinaBlockEntity;

import net.minecraft.world.inventory.ContainerData;

public class EnergyPropData implements ContainerData {

	public static final EnergyPropData of(Supplier<MachinaBlockEntity> data, Consumer<Integer> maxEnergy,
			Consumer<Integer> energy) {
		return new EnergyPropData(data, maxEnergy, energy);
	}

	private final Supplier<MachinaBlockEntity> data;
	private final Consumer<Integer> maxEnergy;
	private final Consumer<Integer> energy;

	private EnergyPropData(Supplier<MachinaBlockEntity> data, Consumer<Integer> maxEnergy, Consumer<Integer> energy) {
		this.data = data;
		this.maxEnergy = maxEnergy;
		this.energy = energy;
	}

	@Override
	public int get(int index) {
		if (index == 0) {
			return data.get().getEnergy();
		} else if (index == 1) {
			return data.get().getMaxEnergy();
		} else {
			throw new IllegalArgumentException("Illegal EnergyPropData index: " + index);
		}
	}

	@Override
	public void set(int index, int value) {
		if (index == 0) {
			this.energy.accept(value);
		} else if (index == 1) {
			this.maxEnergy.accept(value);
		} else {
			throw new IllegalArgumentException("Illegal EnergyPropData index: " + index);
		}
	}

	@Override
	public int getCount() {
		return 2;
	}
}
