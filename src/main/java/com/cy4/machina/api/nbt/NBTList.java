package com.cy4.machina.api.nbt;

import java.util.ArrayList;
import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class NBTList<O extends INBTSerializable<CompoundNBT>> extends ArrayList<O>
		implements INBTSerializable<CompoundNBT> {

	private static final long serialVersionUID = -3021708656511519616L;

	private final transient Function<CompoundNBT, O> deserializer;

	public NBTList(Function<CompoundNBT, O> deserializer) {
		super();
		this.deserializer = deserializer;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			tag.put(String.valueOf(i), get(i).serializeNBT());
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			O element = deserializer.apply(nbt.getCompound(String.valueOf(i)));
			if (i < size()) {
				set(i, element);
			} else {
				add(i, element);
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof NBTList<?>;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
