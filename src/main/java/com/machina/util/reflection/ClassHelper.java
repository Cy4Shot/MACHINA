package com.machina.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

import org.apache.commons.lang.Validate;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void doWithStatics(Class clazz, BiConsumer<String, T> func) {
		for (Field field : clazz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				try {
					func.accept(field.getName(), (T) field.get(null));
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void removeFinalModifier(final Field field) {
		Validate.notNull(field, "field");

		try {
			if (Modifier.isFinal(field.getModifiers())) {
				final Field modifiersField = Field.class.getDeclaredField("modifiers");
				final boolean doForceAccess = !modifiersField.isAccessible();
				if (doForceAccess) {
					modifiersField.setAccessible(true);
				}
				try {
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				} finally {
					if (doForceAccess) {
						modifiersField.setAccessible(false);
					}
				}
			}
		} catch (final NoSuchFieldException | IllegalAccessException e) {
		}
	}
}
