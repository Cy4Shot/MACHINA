package com.machina.planet.attribute.serializers;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.nbt.INBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public abstract class AttributeSerializer<T> {

	Function<Random, T> gen;
	Function<T, T> format;
	Supplier<T> def;
	public final String name;

	protected AttributeSerializer(String name, Supplier<T> def, Function<Random, T> gen) {
		this(name, def, gen, t -> t);
	}

	protected AttributeSerializer(String name, Supplier<T> def, Function<Random, T> gen, Function<T, T> format) {
		this.name = name;
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
		return def.get();
	}

	public abstract INBT save(T data);

	public abstract T load(INBT data);

	@SuppressWarnings("rawtypes")
	public abstract Map<String, ConfigValue> generateConf(ForgeConfigSpec.Builder builder);
}
