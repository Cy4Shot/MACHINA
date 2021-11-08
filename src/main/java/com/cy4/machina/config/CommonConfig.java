package com.cy4.machina.config;

import com.cy4.machina.api.config.BaseTOMLConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class CommonConfig extends BaseTOMLConfig {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	// Bad Cy4, not caring about syntax THIS IS A CONSTANT
	public static final ForgeConfigSpec.ConfigValue<Integer> MIN_PLANETS;
	public static final ForgeConfigSpec.ConfigValue<Integer> MAX_PLANETS;
	
	//@formatter:off
	static {
		BUILDER.push("Config for Machina!");
		
		// WHY is order messed up
		MIN_PLANETS = config("Minimum planets in the starchart.", "minimumPlanets", 5);
		MAX_PLANETS = config("Maximum planets in the starchart.", "maximumPlanets", 10);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}

	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return config(comment, path, defaultValue, true, BUILDER);
	}
	
}
