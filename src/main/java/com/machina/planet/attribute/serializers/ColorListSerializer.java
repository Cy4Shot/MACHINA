package com.machina.planet.attribute.serializers;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.machina.util.color.Color;
import com.machina.util.nbt.BaseNBTList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class ColorListSerializer extends AttributeSerializer<Color[]> {

	private int size;

	public ColorListSerializer(Color def, Function<Random, Color[]> gen, int s) {
		super(new Color[] { def }, gen);
		this.size = s;
	}

	public Function<INBT, Color> colorDeserializer() {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return new Color(((IntNBT) nbt).getAsInt());
			}
			return def[0];
		};
	}

	public static Function<Color, INBT> colorSerializer() {
		return color -> IntNBT.valueOf(color.getRGB());
	}

	@Override
	public INBT save(Color[] data) {
		BaseNBTList<Color, INBT> c = new BaseNBTList<>(colorSerializer(), colorDeserializer());
		c.addAll(Arrays.asList(data));
		return c.serializeNBT();
	}

	@Override
	public Color[] load(INBT data) {
		if (data instanceof CompoundNBT) {
			BaseNBTList<Color, INBT> colors = new BaseNBTList<>(colorSerializer(), colorDeserializer());
			colors.deserializeNBT((CompoundNBT) data);
			return colors.toArray(new Color[size]);
		}
		return def;
	}

}
