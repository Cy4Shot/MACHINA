package com.machina.planet.attribute.serializers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.machina.config.CommonConfig;
import com.machina.util.Color;
import com.machina.util.nbt.BaseNBTList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ColorListSerializer extends AttributeSerializer<Color[]> {
	
	public static Color[] val(String name, String key) {
		int[] colors = (int[]) CommonConfig.attributeConf.get(name).get(key).get();
		Color[] out = new Color[colors.length];
		for(int i = 0; i < colors.length; i++) {
			out[i] = new Color(colors[i]);
		}
		return out;
	}

	private int size;
	
	private final Color[] d;

	public ColorListSerializer(String name, Color[] def, Function<Random, Color[]> gen, int s) {
		super(name, () -> val(name, "def"), gen);
		this.size = s;
		this.d = def;
	}

	public Function<INBT, Color> colorDeserializer() {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return new Color(((IntNBT) nbt).getAsInt());
			}
			return def.get()[0];
		};
	}

	public static Function<Color, INBT> colorSerializer() {
		return color -> IntNBT.valueOf(color.getRGB());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, ConfigValue> generateConf(ForgeConfigSpec.Builder builder) {
		Integer[] out = new Integer[d.length];
		for(int i = 0; i < d.length; i++) {
			out[i] = d[i].getRGB();
		}
		Map<String, ConfigValue> map = new HashMap<>();
		map.put("def", builder.defineList("def", Arrays.asList(out), entry -> true)); 
		return map;
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
		return def.get();
	}
}
