package com.machina.api.util.reflect;

@FunctionalInterface
public interface QuintFunction<P1, P2, P3, P4, P5, R> {
	R apply(P1 a, P2 b, P3 c, P4 d, P5 e);

	public static <T1, T2, T3, T4, T5, S> QuintFunction<T1, T2, T3, T4, T5, S> none() {
		return new QuintFunction<T1, T2, T3, T4, T5, S>() {
			@Override
			public S apply(T1 a, T2 b, T3 c, T4 d, T5 e) {
				return null;
			}
		};
	}
}