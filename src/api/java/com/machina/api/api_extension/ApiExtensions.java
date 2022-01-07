/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.api_extension;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.objectweb.asm.Type;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

@SuppressWarnings("unchecked")
public class ApiExtensions {

	private static volatile Map<Class<?>, Object> EXTENSIONS = Collections.synchronizedMap(new HashMap<>());

	public static synchronized <EXTENSION extends IApiExtendable, IMPL extends EXTENSION> void registerExtension(
			Class<EXTENSION> extensionType, IMPL implementation) {
		if (!EXTENSIONS.containsKey(extensionType)) {
			EXTENSIONS.put(extensionType, implementation);
		} else {
			throw new RuntimeException("Tried to register an ApiExtension for a class which already has that!");
		}
	}

	@Nullable
	public static <EXTENSION extends IApiExtendable> EXTENSION getExtension(Class<EXTENSION> extensionType) {
		return (EXTENSION) EXTENSIONS.get(extensionType);
	}

	public synchronized static <EXTENSION, X extends Exception> void useExtension(Class<EXTENSION> extensionType,
			ExtensionConsumer<EXTENSION, X> callback) throws X {
		withExtension(extensionType, extension -> {
			callback.useExtension(extension);
			return null;
		});
	}

	public static synchronized <R, EXTENSION, X extends Exception> R withExtension(Class<EXTENSION> extensionType,
			ExtensionCallback<R, EXTENSION, X> callback) throws X {
		return callback.withExtension((EXTENSION) EXTENSIONS.get(extensionType));
	}

	private static boolean annotationsProcessed = false;

	public static void registerAnnotationExtensions() {
		if (annotationsProcessed) {
			return;
		}
		annotationsProcessed = true;
		ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream)
				.filter(a -> a.getAnnotationType().equals(Type.getType(ApiExtension.class))
						&& a.getTargetType() == ElementType.FIELD)
				.forEach(data -> {
					try {
						final Class<?> clazz = Class.forName(data.getClassType().getClassName(), false,
								ApiExtensions.class.getClassLoader());
						final Field field = clazz.getDeclaredField(data.getMemberName());
						field.setAccessible(true);
						Class<? extends IApiExtendable> extensionType = field.getAnnotation(ApiExtension.class).value();
						EXTENSIONS.put(extensionType, field.get(null));
					} catch (ClassNotFoundException | NoSuchFieldException | SecurityException
							| IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("Could not register Api Extension", e);
					}
				});
	}
}
