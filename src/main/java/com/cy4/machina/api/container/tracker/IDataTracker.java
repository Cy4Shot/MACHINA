package com.cy4.machina.api.container.tracker;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import net.minecraft.inventory.container.Container;

public interface IDataTracker {

	default IntSupplier getter() {
		return () -> 0;
	}
	
	default IntConsumer setter() {
		return val -> {
			
		};
	}
	
	default void addDataSlots(Container container) {
		//container.addDataSlot(new FunctionalIntReferenceHolder(getter(), setter()));
	}
	
}
