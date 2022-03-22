package com.machina.planet.attribute;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.INBT;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PlanetAttributeType<T> extends ForgeRegistryEntry<PlanetAttributeType<?>> {

	private final String measureUnit;

	public final Function<T, INBT> valueSerializer;
	public final Function<INBT, T> valueDeserializer;
	public final Function<Random, T> generator;

	public PlanetAttributeType(String measureUnit, Function<T, INBT> valueSerializer,
			Function<INBT, T> valueDeserializer, Function<Random, T> generator) {
		this.measureUnit = measureUnit;
		this.valueSerializer = valueSerializer;
		this.valueDeserializer = valueDeserializer;
		this.generator = generator;
	}

	public String getMeasureUnit() { return measureUnit; }
	
	public boolean isShown() { return true; }

}
