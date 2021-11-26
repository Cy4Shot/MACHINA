package com.cy4.machina.api.container.tracker;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class BooleanTracker implements IDataTracker {
	
	private boolean value;
	
	public BooleanTracker(boolean value) {
		this.value = value;
	}

	@Override
	public IntSupplier getter() {
		return () -> value ? 1 : 0;
	}

	@Override
	public IntConsumer setter() {
		return newVal -> {
			if (newVal == 1) {
				value = true;
			} else if (newVal == 0) {
				value = false;
			}
		};
	}
	
	public boolean get() { return this.value; }

}
