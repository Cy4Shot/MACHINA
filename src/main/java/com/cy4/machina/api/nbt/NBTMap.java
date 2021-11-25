/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.nbt;

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
