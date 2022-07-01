package com.machina.planet.attribute.serializers;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class IntSerializer extends AttributeSerializer<Integer> {

	public IntSerializer(Integer def, Function<Random, Integer> gen) {
		super(def, gen);
	}

	@Override
	public INBT save(Integer data) {
		return IntNBT.valueOf(data);
	}

	@Override
	public Integer load(INBT data) {
		if (data instanceof IntNBT) {
			return ((IntNBT) data).getAsInt();
		}
		return def;
	}
}
