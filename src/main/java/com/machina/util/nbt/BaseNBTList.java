package com.machina.util.nbt;

import java.util.ArrayList;
import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unchecked")
public class BaseNBTList<O, ONBT extends INBT> extends ArrayList<O> implements INBTSerializable<CompoundNBT> {

	private static final long serialVersionUID = -3021708656511519616L;

	private final transient Function<O, ONBT> serializer;
	private final transient Function<ONBT, O> deserializer;

	public BaseNBTList(Function<O, ONBT> serializer, Function<ONBT, O> deserializer) {
		super();
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			tag.put(String.valueOf(i), serializer.apply(get(i)));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			O element = deserializer.apply((ONBT) nbt.get(String.valueOf(i)));
			if (i < size()) {
				set(i, element);
			} else {
				add(i, element);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof BaseNBTList<?, ?>;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
