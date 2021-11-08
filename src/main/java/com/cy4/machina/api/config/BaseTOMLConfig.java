package com.cy4.machina.api.config;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public abstract class BaseTOMLConfig {

	//@formatter:on
	protected static <T> ConfigValue<T> config(String comment, String path, T defaultValue,
			boolean addDefaultValueComment, Builder builder) {
//		return addDefaultValueComment
//				? BUILDER.comment(comment + " [Default: " + defaultValue.toString() + "]").define(path, defaultValue)
//				: BUILDER.comment(comment).define(path, defaultValue);
		return addDefaultValueComment
				? builder.comment(comment, "default: " + defaultValue.toString()).define(path, defaultValue)
				: builder.comment(comment).define(path, defaultValue);
	}
	
	protected static ConfigValue<Integer> percentChanceConfig(String comment, String path, int defaultValue,
			boolean addDefaultValueComment, Builder builder) {
		String range = "range: 0 ~ 100";
		return addDefaultValueComment
				? builder.comment(comment, "default: " + defaultValue, range).define(path, defaultValue)
						: builder.comment(comment, range).defineInRange(path, defaultValue, 0, 100);
	}
	
}
