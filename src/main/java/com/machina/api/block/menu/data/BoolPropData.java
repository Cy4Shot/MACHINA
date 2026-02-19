package com.machina.api.block.menu.data;

import java.util.function.Supplier;

import net.minecraft.world.inventory.DataSlot;

public class BoolPropData extends DataSlot {

	public static final BoolPropData of(Supplier<Boolean> data) {
		return new BoolPropData(data);
	}

	private final Supplier<Boolean> data;
	private boolean state;

	private BoolPropData(Supplier<Boolean> data) {
		this.data = data;
	}

	@Override
	public int get() {
		return this.data.get() ? 1 : 0;
	}

	@Override
	public void set(int value) {
		state = value == 1;
	}

	public boolean read() {
		return state;
	}
}
