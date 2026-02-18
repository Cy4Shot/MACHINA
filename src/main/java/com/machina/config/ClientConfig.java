package com.machina.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {

	public static final ModConfigSpec CLIENT_SPEC;

	static {
		ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
		setupConfig(configBuilder);
		CLIENT_SPEC = configBuilder.build();
	}

	// Terminal Options
	public static ModConfigSpec.BooleanValue devMode;

	private static void setupConfig(ModConfigSpec.Builder builder) {
		builder.comment(" The following options are only used for development of the mod.");
		builder.push("Dev Options");
		{
			devMode = builder.define("development_mode", false);
		}
		builder.pop();
	}
}