package com.machina.config.base;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public abstract class BaseTOMLConfig {

	public static <T> ConfigValue<T> config(String comment, String path, T defaultValue,
			boolean addDefaultValueComment, Builder builder) {
		return addDefaultValueComment
				? builder.comment(comment, "default: " + defaultValue.toString()).define(path, defaultValue)
				: builder.comment(comment).define(path, defaultValue);
	}

	public static ConfigValue<Integer> percentChanceConfig(String comment, String path, int defaultValue,
			boolean addDefaultValueComment, Builder builder) {
		String range = "range: 0 ~ 100";
		return addDefaultValueComment
				? builder.comment(comment, "default: " + defaultValue, range).define(path, defaultValue)
				: builder.comment(comment, range).defineInRange(path, defaultValue, 0, 100);
	}

}
