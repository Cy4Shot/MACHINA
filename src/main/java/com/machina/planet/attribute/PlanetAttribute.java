package com.machina.planet.attribute;

import com.machina.registration.registry.PlanetAttributeRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetAttribute<T> implements INBTSerializable<CompoundNBT> {

	private PlanetAttributeType<T> attributeType;
	private T value;
	
	public PlanetAttribute(PlanetAttributeType<T> attributeType, T value) {
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
	
	public T getValue() { return this.value; }

	public PlanetAttributeType<T> getAttributeType() {
		return attributeType;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.put("value", attributeType.valueSerializer.apply(this.value));
		tag.putString("type", attributeType.getRegistryName().toString());
		return tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.attributeType = (PlanetAttributeType<T>) PlanetAttributeRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("type")));
		this.value = attributeType.valueDeserializer.apply(nbt.get("value"));
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static PlanetAttribute<?> fromNBT(CompoundNBT nbt) {
		PlanetAttributeType<?> type = PlanetAttributeRegistry.REGISTRY.getValue(new ResourceLocation(nbt.getString("type")));
		if (type != null) {
			return new PlanetAttribute(type, type.valueDeserializer.apply(nbt.get("value")));
		}
		return null;
	}
	
}
