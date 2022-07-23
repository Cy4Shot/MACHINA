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
	public static ForgeConfigSpec.IntValue maxHeat;
	
	public static ForgeConfigSpec.DoubleValue ammoniaNitrateMult;
	public static ForgeConfigSpec.DoubleValue aluminiumMult;
	public static ForgeConfigSpec.DoubleValue waterMult;

	private static void setupConfig(ForgeConfigSpec.Builder builder) {
		builder.comment(" This category holds options for Starchart Generation.");
		builder.push("Starchart Options");
		minPlanets = builder.defineInRange("min_planets", 5, 2, 20);
		maxPlanets = builder.defineInRange("max_planets", 10, 3, 21);
		builder.pop();
		
		builder.comment(" This category holds options for the rocket.");
		builder.push("Rocket Options");
		ammoniaNitrateMult = builder.defineInRange("ammonia_nitrate_fuel_mult", 50D, 0D, 1000D);
		aluminiumMult = builder.defineInRange("aluminium_fuel_mult", 25D, 0D, 1000D);
		waterMult = builder.defineInRange("water_fuel_mult", 0.8D, 0D, 10D);
		builder.pop();
		
		builder.comment(" Machinery Options.");
		builder.push("Heat Options");
		maxHeat = builder.defineInRange("max_heat", 1000, 0, 2000);
		builder.pop();
	}
}
