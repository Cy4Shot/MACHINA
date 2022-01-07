package com.machina.api.api_extension;

/**
 * Callback for use with {@link ApiExtensions#useExtension(Class)}.
 *
 * @param <E> extension type
 * @param <X> optional exception type thrown by the callback
 */
@FunctionalInterface
public interface ExtensionConsumer<E, X extends Exception> {

	/**
	 * Will be invoked with an extension.
	 *
	 * @param  extension extension to be used only within the scope of this
	 *                   callback.
	 * @throws X         optional exception thrown by this callback.
	 */
	void useExtension(E extension) throws X;
}
