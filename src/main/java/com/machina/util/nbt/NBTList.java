package com.machina.util.nbt;

import java.util.function.Function;

import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

public class NBTList<O extends INBTSerializable<ONBT>, ONBT extends INBT> extends BaseNBTList<O, ONBT> {
	
	private static final long serialVersionUID = -7631328899434821083L;

	public NBTList(Function<ONBT, O> deserializer) {
		super(O::serializeNBT, deserializer);
	}

}
