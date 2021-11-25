/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.util.helper;

public final class ClassHelper {

	/**
	 * Ugly hack to transform a class of T into a wildcard typed T class <br>
	 * <strong>THIS DOES NO CHECKING. USE AT YOUR OWN RISK</strong>
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> withWildcard(Class<?> cls) {
		return (Class<T>) cls;
	}

}
