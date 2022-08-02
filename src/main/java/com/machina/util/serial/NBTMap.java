package com.machina.util.serial;

import java.util.function.Function;

import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

public class NBTMap<K extends INBTSerializable<DK>, V extends INBTSerializable<DV>, DK extends INBT, DV extends INBT>
extends BaseNBTMap<K, V, DK, DV> {

	private static final long serialVersionUID = -4397124152173696864L;

	public NBTMap(Function<DK, K> keyDeserializer, Function<DV, V> valueDeserializer) {
		super(K::serializeNBT, V::serializeNBT, keyDeserializer, valueDeserializer);
	}

}
