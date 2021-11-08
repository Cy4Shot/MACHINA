package com.cy4.machina.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class MachinaConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> minPlanets;
	public static final ForgeConfigSpec.ConfigValue<Integer> maxPlanets;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> LOW_GRAVITY_AIR_TIME;
	
	//@formatter:off
	static {
		BUILDER.push("Config for Machina!");
		
		minPlanets = BUILDER.comment("Minimum planets in the starchart. [Default: 5]").define("Minimum Planets", 5);
		maxPlanets = BUILDER.comment("Maximum planets in the starchart. [Default: 10]").define("Maximum Planets", 10);
		
		BUILDER.pop();
		
		BUILDER.push("planetTraits");
		
		LOW_GRAVITY_AIR_TIME = config("The amount of ticks the players will stay in the air when they jump in a planet with the Low Gravity trait.", "lowGravityAirTime", 25);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
	//@formatter:on
		private static <T> ConfigValue<T> config(String comment, String path, T defaultValue,
				boolean addDefaultValueComment) {
//			return addDefaultValueComment
//					? BUILDER.comment(comment + " [Default: " + defaultValue.toString() + "]").define(path, defaultValue)
//					: BUILDER.comment(comment).define(path, defaultValue);
			return addDefaultValueComment
					? BUILDER.comment(comment, "default: " + defaultValue.toString()).define(path, defaultValue)
					: BUILDER.comment(comment).define(path, defaultValue);
		}

		private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
			return config(comment, path, defaultValue, true);
		}
}
