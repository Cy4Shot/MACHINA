package com.machina.config.base;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class TOMLConfigBuilder extends ForgeConfigSpec.Builder {
	
	public <T> ConfigValue<T> config(String comment, String path, T defaultValue,
			boolean addDefaultValueComment) {
		return addDefaultValueComment
				? this.comment(comment, "default: " + defaultValue.toString()).define(path, defaultValue)
				: this.comment(comment).define(path, defaultValue);
	}

	public ConfigValue<Integer> percentChanceConfig(String comment, String path, int defaultValue,
			boolean addDefaultValueComment) {
		String range = "range: 0 ~ 100";
		return addDefaultValueComment
				? this.comment(comment, "default: " + defaultValue, range).define(path, defaultValue)
				: this.comment(comment, range).defineInRange(path, defaultValue, 0, 100);
	}

}
