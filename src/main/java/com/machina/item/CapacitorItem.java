package com.machina.item;

import java.util.function.Supplier;

import com.machina.api.item.EnergyItem;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CapacitorItem extends EnergyItem {

	private final Supplier<ModConfigSpec.IntValue> capacity;

	public CapacitorItem(Properties props, Supplier<ModConfigSpec.IntValue> capacity) {
		super(props);
		this.capacity = capacity;
	}

	@Override
	public int getMaxEnergy() {
		return this.capacity.get().get();
	}
}