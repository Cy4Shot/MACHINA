package com.machina.api.util.reflect;

@FunctionalInterface
public interface QuintFunction<P1, P2, P3, P4, P5, R> {
    R apply(P1 a, P2 b, P3 c, P4 d, P5 e);

    static <T1, T2, T3, T4, T5, S> QuintFunction<T1, T2, T3, T4, T5, S> none() {
        return (a, b, c, d, e) -> null;
    }
}