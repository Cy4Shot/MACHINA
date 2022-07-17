package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.INBT;

public abstract class AttributeSerializer<T> {
	
	Function<Random, T> gen;
	Function<T, T> format;
	T def;
	
	public AttributeSerializer(T def, Function<Random, T> gen) {
		this(def, gen, t -> t);
	}
	
	public AttributeSerializer(T def, Function<Random, T> gen, Function<T, T> format) {
		this.gen = gen;
		this.def = def;
		this.format = format;
	}
	
	public T rand(Random r) {
		return gen.apply(r);
	}
	
	public T formatted(T in) {
		return format.apply(in);
	}
	
	public T def() {
		return def;
	}
	
	public abstract INBT save(T data);
	public abstract T load(INBT data);
}
