package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		CLIENT_SPEC = configBuilder.build();
	}

	// Terminal Options
	public static ForgeConfigSpec.BooleanValue devMode;

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
		builder.comment(" The following options are only used for development of the mod.");
		builder.push("Dev Options");
		{
			devMode = builder.define("development_mode", false);
		}
		builder.pop();
	}
}