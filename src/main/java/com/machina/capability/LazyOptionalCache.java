package com.machina.capability;

import java.util.function.Supplier;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class LazyOptionalCache<T> {

	private LazyOptional<T> cache;

	public LazyOptionalCache() {
		cache = LazyOptional.empty();
	}
	
	public LazyOptionalCache(NonNullSupplier<T> thing) {
		cache = LazyOptional.of(thing);
	}

	public LazyOptional<T> get() {
		return cache;
	}

	public void revalidate(Supplier<T> cachePopulator) {
		invalidate();
		cache = LazyOptional.of(cachePopulator::get);
	}

	public void invalidate() {
		cache.invalidate();
	}
}
