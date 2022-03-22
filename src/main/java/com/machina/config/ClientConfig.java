package com.machina.config;

import com.machina.Machina;
import com.machina.config.base.BaseTOMLConfig;
import com.machina.config.base.TOMLConfigBuilder;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ClientConfig extends BaseTOMLConfig {

	public static final TOMLConfigBuilder BUILDER = new TOMLConfigBuilder();
	public static ForgeConfigSpec spec;
	
	public static ForgeConfigSpec.ConfigValue<Boolean> disableExperimentalSettingsScreen;

	//@formatter:off
	public static void register() {
		BUILDER.push("machinaConfig");
		
		disableExperimentalSettingsScreen = config("If the experimentals settings screen should be disabled.",
				"disableExperimentalSettingsScreen", true);
		
		
		
		BUILDER.pop();
		spec = BUILDER.build();
		
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.spec, Machina.MOD_ID + "/client.toml");
    }

	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return BUILDER.config(comment, path, defaultValue, true);
	}

}
