package com.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
	public static final ForgeConfigSpec COMMON_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		COMMON_SPEC = configBuilder.build();
	}
	
	public static ForgeConfigSpec.IntValue minPlanets;
	public static ForgeConfigSpec.IntValue maxPlanets;

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
		builder.comment(" This category holds options for Starchart Generation.");
	    builder.push("Starchart Options");
	    minPlanets = builder.defineInRange("min_planets", 5, 2, 20);
	    maxPlanets = builder.defineInRange("max_planets", 10, 3, 21);
	    builder.pop();
	}
}
