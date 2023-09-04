package com.machina.api.cap;

import net.minecraft.nbt.CompoundTag;

public interface ICustomStorage {
	void setChanged(Runnable runnable);

	String getTag();

	CompoundTag serialize();

	void deserialize(CompoundTag nbt);
}