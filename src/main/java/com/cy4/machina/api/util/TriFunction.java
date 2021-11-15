package com.cy4.machina.api.util;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {
	R apply(A a, B b, C c);
}
