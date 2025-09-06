package com.machina.api.util.reflect;

import com.machina.Machina;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiConsumer;

public class ClassHelper {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> void doWithStatics(Class clazz, BiConsumer<String, T> func) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                try {
                    func.accept(field.getName(), (T) field.get(null));
                } catch (Exception e) {
                    Machina.LOGGER.error(e);
                }
            }
        }
    }
}
