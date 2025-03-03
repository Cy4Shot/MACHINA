package com.machina.api.util.reflect;

@FunctionalInterface
public interface QuadFunction<P1, P2, P3, P4, R> {
	R apply(P1 a, P2 b, P3 c, P4 d);
	
	public static <T1, T2, T3, T4, S> QuadFunction<T1, T2, T3, T4, S> none() {
		return new QuadFunction<T1, T2, T3, T4, S>() {
			@Override
			public S apply(T1 a, T2 b, T3 c, T4 d) {
				return null;
			}
		};
	}
}