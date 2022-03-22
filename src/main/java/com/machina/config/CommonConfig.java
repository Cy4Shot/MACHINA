package com.machina.config;

import com.machina.Machina;
import com.machina.config.base.BaseTOMLConfig;
import com.machina.config.base.TOMLConfigBuilder;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class CommonConfig extends BaseTOMLConfig {

	public static final TOMLConfigBuilder BUILDER = new TOMLConfigBuilder();
	public static ForgeConfigSpec spec;

	public static ForgeConfigSpec.ConfigValue<Integer> minPlanets;
	public static ForgeConfigSpec.ConfigValue<Integer> maxPlanets;

	//@formatter:off
	public static void register() {
		BUILDER.push("machinaConfig");

		minPlanets = config("Minimum planets in the starchart.", "minimumPlanets", 5);
		maxPlanets = config("Maximum planets in the starchart.", "maximumPlanets", 10);
		
		BUILDER.pop();
		spec = BUILDER.build();
		
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.spec, Machina.MOD_ID + "/common.toml");
    }

	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return BUILDER.config(comment, path, defaultValue, true);
	}

}
