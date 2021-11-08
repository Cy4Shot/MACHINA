package com.cy4.machina.config;

import com.cy4.machina.api.config.BaseTOMLConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfig extends BaseTOMLConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_EXPERIMENTAL_SETTINGS_SCREEN;
	
	static {
		BUILDER.push("generalConfig");
		
		DISABLE_EXPERIMENTAL_SETTINGS_SCREEN = config("If the experimentals settings screen should be disabled.", "disableExperimentalSettingsScreen", true);
		
		SPEC = BUILDER.build();
	}
	
	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return config(comment, path, defaultValue, true, BUILDER);
	}
	
}
