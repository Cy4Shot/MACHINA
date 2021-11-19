package com.cy4.machina.util.helper;

public final class ClassHelper {

	/**
	 * Ugly hack to transform a class of T into a wildcard typed T class <br>
	 * <strong>THIS DOES NO CHECKING. USE AT YOUR OWN RISK</strong>
	 * @param <T>
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> withWildcard(Class<?> cls) { return (Class<T>) cls; }
	
}
