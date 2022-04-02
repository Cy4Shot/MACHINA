package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		CLIENT_SPEC = configBuilder.build();
	}

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
	}

}
