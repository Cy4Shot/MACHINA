/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
