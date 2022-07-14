package com.machina.planet.attribute.serializers;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.machina.util.nbt.BaseNBTList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;

public class DoubleListSerializer extends AttributeSerializer<Double[]> {

	private int size;

	public DoubleListSerializer(Double def, Function<Random, Double[]> gen, int s) {
		super(new Double[] { def }, gen);
		this.size = s;
	}

	public Function<INBT, Double> doubleDeserializer() {
		return nbt -> {
			if (nbt instanceof DoubleNBT) {
				return ((DoubleNBT) nbt).getAsDouble();
			}
			return def[0];
		};
	}

	public static Function<Double, INBT> doubleSerializer() {
		return val -> DoubleNBT.valueOf(val);
	}

	@Override
	public INBT save(Double[] data) {
		BaseNBTList<Double, INBT> doubles = new BaseNBTList<>(doubleSerializer(), doubleDeserializer());
		doubles.addAll(Arrays.asList(data));
		return doubles.serializeNBT();
	}

	@Override
	public Double[] load(INBT data) {
		if (data instanceof CompoundNBT) {
			BaseNBTList<Double, INBT> doubles = new BaseNBTList<>(doubleSerializer(), doubleDeserializer());
			doubles.deserializeNBT((CompoundNBT) data);
			return doubles.toArray(new Double[size]);
		}
		return def;
	}
}
