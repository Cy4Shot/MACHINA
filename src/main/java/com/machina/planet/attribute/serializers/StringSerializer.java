package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public class StringSerializer extends AttributeSerializer<String> {

	public StringSerializer(String def, Function<Random, String> gen) {
		super(def, gen);
	}

	@Override
	public INBT save(String data) {
		return StringNBT.valueOf(data);
	}

	@Override
	public String load(INBT data) {
		if (data instanceof StringNBT) {
			return ((StringNBT) data).getAsString();
		}
		return def;
	}
}
