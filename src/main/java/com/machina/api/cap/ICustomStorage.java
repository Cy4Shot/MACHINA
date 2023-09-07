package com.machina.api.cap;

import net.minecraft.nbt.CompoundTag;

/**
 * An interface that all capability storages should extend.
 * 
 * @author Cy4
 * 
 * @since Machina v0.1.0
 */
public interface ICustomStorage {
	void setChanged(Runnable runnable);

	String getTag();

	CompoundTag serialize();

	void deserialize(CompoundTag nbt);
}