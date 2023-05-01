package com.machina.planet.attribute;

import com.machina.planet.attribute.serializers.AttributeSerializer;
import com.machina.util.text.StringUtils;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class AttributeType<T> extends ForgeRegistryEntry<AttributeType<?>> {

	private final String measureUnit;
	public final AttributeSerializer<T> ser;

	public AttributeType(String measureUnit, AttributeSerializer<T> ser) {
		this.measureUnit = measureUnit;
		this.ser = ser;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public boolean isShown() {
		return true;
	}

	public String getName() {
		return StringUtils.translate(
				this.getRegistryName().getNamespace() + ".planet_attribute." + this.getRegistryName().getPath());
	}

}
