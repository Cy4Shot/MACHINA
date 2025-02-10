package com.machina.api.util.reflect;

@FunctionalInterface
public interface QuadFunction<P1, P2, P3, P4, R> {
	R apply(P1 a, P2 b, P3 c, P4 d);
}