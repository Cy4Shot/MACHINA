package com.machina.config;

import com.machina.config.base.BaseTOMLConfig;
import com.machina.config.base.TOMLConfigBuilder;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ServerConfig extends BaseTOMLConfig {

	public static final TOMLConfigBuilder BUILDER = new TOMLConfigBuilder();
	public static ForgeConfigSpec spec;
	
	//@formatter:off
	public static void register() {
		BUILDER.push("machinaConfig");
		
		BUILDER.pop();
		spec = BUILDER.build();
		
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.spec, "machina-server.toml");
    }

	@SuppressWarnings("unused")
	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return BUILDER.config(comment, path, defaultValue, true);
	}

}
