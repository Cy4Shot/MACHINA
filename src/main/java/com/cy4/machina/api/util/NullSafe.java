package com.cy4.machina.api.util;

import java.util.Objects;

import javax.annotation.Nonnull;

public class NullSafe<O> {

	@Nonnull
	private O object;

	public NullSafe(@Nonnull O defaultValue) {
		set(defaultValue);
	}

	/**
	 * Gets the value of the object
	 * 
	 * @return
	 */
	@Nonnull
	public O get() {
		return object;
	}

	/**
	 * Sets the value of the object
	 * 
	 * @param object
	 * @throws NullPointerException if the new object is null
	 */
	public void set(@Nonnull O object) {
		this.object = Objects.requireNonNull(object, () -> "Object cannot be null!");
	}
}
