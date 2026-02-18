package com.machina.api.cap;

import net.minecraft.nbt.CompoundTag;

public interface IMachinaStorage {
	CompoundTag serialize();

	void deserialize(CompoundTag nbt);
}
