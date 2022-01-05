package com.machina.api.api_extension;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class ApiExtensions {

	private static final Map<Class<?>, Object> EXTENSIONS = new HashMap<>();

	public static <EXTENSION extends IApiExtendable, IMPL extends EXTENSION> void registerExtension(
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

	public static <EXTENSION, X extends Exception> void useExtension(Class<EXTENSION> extensionType,
			ExtensionConsumer<EXTENSION, X> callback) throws X {
		withExtension(extensionType, extension -> {
			callback.useExtension(extension);
			return null;
		});
	}

	public static <R, EXTENSION, X extends Exception> R withExtension(Class<EXTENSION> extensionType,
			ExtensionCallback<R, EXTENSION, X> callback) throws X {
		return callback.withExtension((EXTENSION) EXTENSIONS.get(extensionType));
	}
}
