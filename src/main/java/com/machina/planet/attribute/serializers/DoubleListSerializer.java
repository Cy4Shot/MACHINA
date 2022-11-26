package com.machina.planet.attribute.serializers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.machina.util.serial.BaseNBTList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class DoubleListSerializer extends AttributeSerializer<Double[]> {

	private int size;
	public Function<RegistryKey<World>, Double[]> func;

	public DoubleListSerializer(Function<RegistryKey<World>, Double[]> def, Function<Random, Double[]> gen, int s) {
		super("", () -> def.apply(World.OVERWORLD), gen);
		this.size = s;
		this.func = def;
	}

	public Function<INBT, Double> doubleDeserializer() {
		return nbt -> {
			if (nbt instanceof DoubleNBT) {
				return ((DoubleNBT) nbt).getAsDouble();
			}
			return def.get()[0];
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
		return def.get();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, ConfigValue> generateConf(Builder builder) {
		return new HashMap<>();
	}
}
