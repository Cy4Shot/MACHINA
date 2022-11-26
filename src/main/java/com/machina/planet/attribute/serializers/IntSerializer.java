package com.machina.planet.attribute.serializers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.config.CommonConfig;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class IntSerializer extends AttributeSerializer<Integer> {

	public static Function<Random, Integer> random(Supplier<Integer> min, Supplier<Integer> max) {
		return r -> min.get() + r.nextInt(max.get() - min.get() + 1);
	}

	public static int val(String name, String key) {
		return (int) CommonConfig.attributeConf.get(name).get(key).get();
	}

	private final int d, mi, ma;

	public IntSerializer(String name, int d, Function<Random, Integer> gen) {
		super(name, () -> val(name, "def"), gen);
		this.d = d;
		this.mi = -1;
		this.ma = -1;
	}

	public IntSerializer(String name, int d, int mi, int ma) {
		super(name, () -> val(name, "def"), random(() -> val(name, "min"), () -> val(name, "max")));
		this.d = d;
		this.mi = mi;
		this.ma = ma;
	}

	public IntSerializer(String name, int d, int mi, int ma, Function<Integer, Integer> format) {
		super(name, () -> val(name, "def"), random(() -> val(name, "min"), () -> val(name, "max")), format);
		this.d = d;
		this.mi = mi;
		this.ma = ma;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, ConfigValue> generateConf(ForgeConfigSpec.Builder builder) {
		Map<String, ConfigValue> map = new HashMap<>();
		map.put("def", builder.defineInRange("def", d, -999999, 999999));
		map.put("min", builder.defineInRange("min", mi, -999999, 999999));
		map.put("max", builder.defineInRange("max", ma, -999999, 999999));
		return map;
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
		return def.get();
	}
}
