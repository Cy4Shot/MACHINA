package com.machina.capability;

import net.minecraft.nbt.CompoundTag;

public interface ICustomStorage {
	public void setChanged(Runnable runnable);

	public String getTag();

	CompoundTag serialize();

	void deserialize(CompoundTag nbt);
}