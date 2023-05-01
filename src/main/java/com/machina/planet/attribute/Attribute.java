package com.machina.planet.attribute;

import com.machina.registration.registry.AttributeRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class Attribute<T> implements INBTSerializable<CompoundNBT> {

	private AttributeType<T> attributeType;
	private T value;

	public Attribute(AttributeType<T> attributeType, T value) {
		this.attributeType = attributeType;
		this.value = value;
	}

	void setValue(T value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public void set(Object v) {
		setValue((T) v);
	}

	public T getValue() {
		return this.value;
	}
	
	public T getValueFormatted() {
		return this.attributeType.ser.formatted(this.value);
	}

	public AttributeType<T> getAttributeType() {
		return attributeType;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.put("value", attributeType.ser.save(this.value));
		tag.putString("type", attributeType.getRegistryName().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.attributeType = (AttributeType<T>) AttributeRegistry.REGISTRY
				.getValue(new ResourceLocation(nbt.getString("type")));
		this.value = attributeType.ser.load(nbt.get("value"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Attribute<?> fromNBT(CompoundNBT nbt) {
		AttributeType<?> type = AttributeRegistry.REGISTRY
				.getValue(new ResourceLocation(nbt.getString("type")));
		if (type != null) {
			return new Attribute(type, type.ser.load(nbt.get("value")));
		}
		return null;
	}

}
