package com.machina.api.util.reflect;

import java.util.function.Supplier;

@FunctionalInterface
public interface BindableFunction<T, R> {
    R apply(T t);

    default Supplier<R> bind(T t) {
        return () -> apply(t);
    }
    
    default Supplier<R> bind(Supplier<T> t) {
        return () -> apply(t.get());
    }
}