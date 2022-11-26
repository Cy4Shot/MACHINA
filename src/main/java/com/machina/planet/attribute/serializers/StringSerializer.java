package com.machina.planet.attribute.serializers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import com.machina.config.CommonConfig;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class StringSerializer extends AttributeSerializer<String> {
	
	public static String val(String name, String key) {
		return (String) CommonConfig.attributeConf.get(name).get(key).get();
	}
	
	private final String d;

	public StringSerializer(String name, String def, Function<Random, String> gen) {
		super(name, () -> val(name, "def"), gen);
		this.d = def;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, ConfigValue> generateConf(ForgeConfigSpec.Builder builder) {
		Map<String, ConfigValue> map = new HashMap<>();
		map.put("def", builder.define("def", d));
		return map;
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
		return def.get();
	}
}
