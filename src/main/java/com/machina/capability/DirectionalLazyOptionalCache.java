package com.machina.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;

// https://github.com/henkelmax/pipez/blob/1.16.5/src/main/java/de/maxhenkel/pipez/utils/DirectionalLazyOptionalCache.java
public class DirectionalLazyOptionalCache<T> {

	protected Map<Direction, LazyOptional<T>> cache;

	public DirectionalLazyOptionalCache() {
		cache = new HashMap<>();
		for (Direction side : Direction.values()) {
			cache.put(side, LazyOptional.empty());
		}
	}

	public LazyOptional<T> get(Direction side) {
		return cache.get(side);
	}

	public void revalidate(Direction side, Function<Direction, Boolean> validFunction,
			Function<Direction, T> cachePopulator) {
		cache.get(side).invalidate();
		if (validFunction.apply(side)) {
			cache.put(side, LazyOptional.of(() -> cachePopulator.apply(side)));
		} else {
			cache.put(side, LazyOptional.empty());
		}
	}

	public void revalidate(Function<Direction, Boolean> validFunction, Function<Direction, T> cachePopulator) {
		for (Direction side : Direction.values()) {
			revalidate(side, validFunction, cachePopulator);
		}
	}

	public void invalidate() {
		cache.values().forEach(LazyOptional::invalidate);
	}

}
