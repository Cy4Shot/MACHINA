package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.INBT;

public abstract class AttributeSerializer<T> {
	
	Function<Random, T> gen;
	T def;
	
	public AttributeSerializer(T def, Function<Random, T> gen) {
		this.gen = gen;
		this.def = def;
	}
	
	public T rand(Random r) {
		return gen.apply(r);
	}
	
	public abstract INBT save(T data);
	public abstract T load(INBT data);
}
