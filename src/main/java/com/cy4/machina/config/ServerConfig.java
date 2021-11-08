package com.cy4.machina.config;

import com.cy4.machina.api.config.BaseTOMLConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ServerConfig extends BaseTOMLConfig {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<Integer> LOW_GRAVITY_AIR_TIME;
	public static final ConfigValue<Integer> SUPERHOT_FIRE_CHANCE;
	
	static {
		BUILDER.push("planetTraits");
		
		LOW_GRAVITY_AIR_TIME = config("The amount of ticks the players will stay in the air when they jump in a planet with the Low Gravity trait.", "lowGravityAirTime", 25);
		SUPERHOT_FIRE_CHANCE = percentChanceConfig("The chance that a player will get put on fire for 1 second, every tick if the player is in a planet with the Superhot trait.", "superhotFireChance", 10, true, BUILDER);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}

	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return config(comment, path, defaultValue, true, BUILDER);
	}

	
}
