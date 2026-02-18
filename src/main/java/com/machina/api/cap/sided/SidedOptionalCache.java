package com.machina.api.cap.sided;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class SidedOptionalCache<T> {

    protected final Map<Direction, Optional<T>> cache;

    public SidedOptionalCache() {
        cache = new HashMap<>();
        for (Direction dir : Direction.values()) {
            cache.put(dir, Optional.empty());
        }
    }

    public Optional<T> get(Direction side) {
        return cache.get(side);
    }

    public void revalidate(Direction side, Function<Direction, Boolean> validFunction,
            Function<Direction, T> cachePopulator) {
        cache.put(side, validFunction.apply(side) ? Optional.of(cachePopulator.apply(side)) : Optional.empty());
    }

    public void revalidate(Function<Direction, Boolean> validFunction, Function<Direction, T> cachePopulator) {
        for (Direction dir : Direction.values()) {
            revalidate(dir, validFunction, cachePopulator);
        }
    }
}