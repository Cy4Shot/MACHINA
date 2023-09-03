package com.machina.capability;

import net.minecraft.nbt.CompoundTag;

public interface ICustomStorage {
	void setChanged(Runnable runnable);

	String getTag();

	CompoundTag serialize();

	void deserialize(CompoundTag nbt);
}