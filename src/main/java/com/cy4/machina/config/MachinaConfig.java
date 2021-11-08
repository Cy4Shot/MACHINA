package com.cy4.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MachinaConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> minPlanets;
	public static final ForgeConfigSpec.ConfigValue<Integer> maxPlanets;
	
	static {
		BUILDER.push("Config for Machina!");
		
		minPlanets = BUILDER.comment("Minimum planets in the starchart. [Default: 5]").define("Minimum Planets", 5);
		maxPlanets = BUILDER.comment("Maximum planets in the starchart. [Default: 10]").define("Maximum Planets", 10);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
