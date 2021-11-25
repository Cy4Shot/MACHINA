/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.nbt;

import java.util.function.Function;

import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

public class NBTList<O extends INBTSerializable<ONBT>, ONBT extends INBT> extends BaseNBTList<O, ONBT> {
	
	private static final long serialVersionUID = -7631328899434821083L;

	public NBTList(Function<ONBT, O> deserializer) {
		super(O::serializeNBT, deserializer);
	}

}
