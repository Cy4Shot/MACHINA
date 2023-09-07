package com.machina.api.cap;

import java.util.function.Supplier;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

/**
 * A wrapper around the {@link LazyOptional} object for use in capabilities.
 * 
 * @author Cy4Shot
 * 
 * @since Machina v0.1.0
 *
 * @param <T> The object to store in the lazy optional.
 */
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
