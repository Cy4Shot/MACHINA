package com.cy4.machina.api.nbt;

import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class CompoundNBTMap<K extends INBTSerializable<CompoundNBT>, V extends INBTSerializable<CompoundNBT>>
		extends NBTMap<K, V, CompoundNBT, CompoundNBT> {

	private static final long serialVersionUID = -1401395757720124733L;

	public CompoundNBTMap(Function<CompoundNBT, K> keyDeserializer, Function<CompoundNBT, V> valueDeserializer) {
		super(keyDeserializer, valueDeserializer);
	}

}
