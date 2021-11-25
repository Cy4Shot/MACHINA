/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.config;

import com.cy4.machina.api.config.BaseTOMLConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfig extends BaseTOMLConfig {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_EXPERIMENTAL_SETTINGS_SCREEN;

	static {
		BUILDER.push("generalConfig");

		DISABLE_EXPERIMENTAL_SETTINGS_SCREEN = config("If the experimentals settings screen should be disabled.",
				"disableExperimentalSettingsScreen", true);

		SPEC = BUILDER.build();
	}

	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return config(comment, path, defaultValue, true, BUILDER);
	}

}
