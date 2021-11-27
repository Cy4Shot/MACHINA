/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.config;

import com.machina.api.config.BaseTOMLConfig;

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
