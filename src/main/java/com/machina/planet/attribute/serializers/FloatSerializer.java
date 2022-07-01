package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;

public class FloatSerializer extends AttributeSerializer<Float> {

	public FloatSerializer(Float def, Function<Random, Float> gen) {
		super(def, gen);
	}

	@Override
	public INBT save(Float data) {
		return FloatNBT.valueOf(data);
	}

	@Override
	public Float load(INBT data) {
		if (data instanceof FloatNBT) {
			return ((FloatNBT) data).getAsFloat();
		}
		return def;
	}
}
