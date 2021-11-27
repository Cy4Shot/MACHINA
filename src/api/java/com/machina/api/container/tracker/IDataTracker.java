package com.machina.api.container.tracker;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import com.google.common.collect.Lists;
import com.machina.api.util.FunctionalIntReferenceHolder;

import net.minecraft.util.IntReferenceHolder;

public interface IDataTracker {

	default IntSupplier getter() {
		return () -> 0;
	}
	
	default IntConsumer setter() {
		return val -> {
			
		};
	}
	
	default java.util.List<IntReferenceHolder> getDataSlots() {
		return Lists.newArrayList(new FunctionalIntReferenceHolder(getter(), setter()));
	}
	
}
