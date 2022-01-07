package com.machina.api.api_extension;

/**
 * Callback for use with {@link ApiExtensions#withExtension}.
 *
 * @param <R> return type
 * @param <E> extension type
 * @param <X> optional exception type thrown by the callback
 */
@FunctionalInterface
public interface ExtensionCallback<R, E, X extends Exception> {

	/**
	 * Will be invoked with an extension.
	 *
	 * @param  extension extension to be used only within the scope of this
	 *                   callback.
	 * @return           the return value of the callback
	 * @throws X         optional exception thrown by this callback.
	 */
	R withExtension(E extension) throws X;
}
