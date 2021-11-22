package com.cy4.machina.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author matyrobbrt
 *
 */
public class ReflectionHelper {

	public static ArrayList<Field> getFieldsAnnotatedWith(ArrayList<Class<?>> classes,
			Class<? extends Annotation> annotation) {
		ArrayList<Field> fields = new ArrayList<>();
		classes.forEach(clazz -> {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(annotation)) {
					fields.add(field);
				}
			}
		});
		return fields;
	}

	public static ArrayList<Method> getMethodsAnnotatedWith(ArrayList<Class<?>> classes,
			Class<? extends Annotation> annotation) {
		ArrayList<Method> methods = new ArrayList<>();
		classes.forEach(clazz -> {
			for (Method method : clazz.getDeclaredMethods()) {
				method.setAccessible(true);
				if (method.isAnnotationPresent(annotation)) {
					methods.add(method);
				}
			}
		});
		return methods;
	}

}
