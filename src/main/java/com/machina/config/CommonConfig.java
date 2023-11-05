package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
	public static ForgeConfigSpec COMMON_SPEC;

	public static void init() {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		COMMON_SPEC = configBuilder.build();
	}

	private static void setupConfig(ForgeConfigSpec.Builder builder) {

	}
}