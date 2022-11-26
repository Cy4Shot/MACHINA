package com.machina.planet.attribute.serializers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.config.CommonConfig;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class FloatSerializer extends AttributeSerializer<Float> {

	public static Function<Random, Float> random(Supplier<Float> min, Supplier<Float> max) {
		return r -> (float) min.get() + r.nextFloat() * ((float)  max.get() - (float)  min.get());
	}

	public static float val(String name, String key) {
		return ((Double) CommonConfig.attributeConf.get(name).get(key).get()).floatValue();
	}

	private final float d, mi, ma;

	public FloatSerializer(String name, float d, float mi, float ma) {
		super(name, () -> val(name, "def"), random(() -> val(name, "min"), () -> val(name, "max")));
		this.d = d;
		this.mi = mi;
		this.ma = ma;
	}

	public FloatSerializer(String name, float d, float mi, float ma, Function<Float, Float> format) {
		super(name, () -> val(name, "def"), random(() -> val(name, "min"), () -> val(name, "max")), format);
		this.d = d;
		this.mi = mi;
		this.ma = ma;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, ConfigValue> generateConf(ForgeConfigSpec.Builder builder) {
		Map<String, ConfigValue> map = new HashMap<>();
		map.put("def", builder.defineInRange("def", d, -999999f, 999999f));
		map.put("min", builder.defineInRange("min", mi, -999999f, 999999f));
		map.put("max", builder.defineInRange("max", ma, -999999f, 999999f));
		return map;
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
		return def.get();
	}

}
