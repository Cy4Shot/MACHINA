package com.cy4.machina.api.nbt;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class NBTMap<K extends INBTSerializable<CompoundNBT>, V extends INBTSerializable<CompoundNBT>>
		extends LinkedHashMap<K, V> implements INBTSerializable<CompoundNBT> {

	private static final long serialVersionUID = -4666755568562981979L;

	private final transient Function<CompoundNBT, K> keyDeserializer;
	private final transient Function<CompoundNBT, V> valueDeserializer;
	
	public NBTMap(Function<CompoundNBT, K> keyDeserializer, Function<CompoundNBT, V> valueDeserializer) {
		super();
		this.keyDeserializer = keyDeserializer;
		this.valueDeserializer = valueDeserializer;
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			CompoundNBT pairTag = new CompoundNBT();
			pairTag.put("key", getKeys().get(i).serializeNBT());
			pairTag.put("value", getValues().get(i).serializeNBT());
			tag.put(String.valueOf(i), pairTag);
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			CompoundNBT pairNbt = nbt.getCompound(String.valueOf(i));
			K key = keyDeserializer.apply(pairNbt.getCompound("key"));
			V value = valueDeserializer.apply(pairNbt.getCompound("value"));
			put(key, value);
		}
	}
	
	public List<K> getKeys() {
		return new LinkedList<>(keySet());
	}
	
	public List<V> getValues() {
		return new LinkedList<>(values());
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof NBTMap<?, ?>;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
