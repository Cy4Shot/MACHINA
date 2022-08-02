package com.machina.util.serial;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import net.minecraftforge.common.util.INBTSerializable;

/**
 * 
 * @author matyrobbrt
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values
 * @param <KNBT> the NBT type used for the keys
 * @param <VNBT> the NBT type used for the values
 */
@SuppressWarnings("unchecked")
public class BaseNBTMap<K, V, KNBT extends INBT, VNBT extends INBT> extends LinkedHashMap<K, V>
		implements INBTSerializable<CompoundNBT> {

	private static final long serialVersionUID = -4666755568562981979L;

	private final transient Function<K, KNBT> keySerializer;
	private final transient Function<V, VNBT> valueSerializer;

	private final transient Function<KNBT, K> keyDeserializer;
	private final transient Function<VNBT, V> valueDeserializer;

	public BaseNBTMap(Function<K, KNBT> keySerializer, Function<V, VNBT> valueSerializer, Function<KNBT, K> keyDeserializer,
			Function<VNBT, V> valueDeserializer) {
		super();
		this.keySerializer = keySerializer;
		this.valueSerializer = valueSerializer;
		this.keyDeserializer = keyDeserializer;
		this.valueDeserializer = valueDeserializer;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			CompoundNBT pairTag = new CompoundNBT();
			pairTag.put("key", keySerializer.apply(getKeys().get(i)));
			pairTag.put("value", valueSerializer.apply(getValues().get(i)));
			tag.put(String.valueOf(i), pairTag);
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			CompoundNBT pairNbt = nbt.getCompound(String.valueOf(i));
			K key = keyDeserializer.apply((KNBT) pairNbt.get("key"));
			V value = valueDeserializer.apply((VNBT) pairNbt.get("value"));
			put(key, value);
		}
	}

	public List<K> getKeys() { return new LinkedList<>(keySet()); }

	public List<V> getValues() { return new LinkedList<>(values()); }

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof NBTMap<?, ?, ?, ?>;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
