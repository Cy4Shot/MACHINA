/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.config;

import com.machina.Machina;
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
		
		Machina.ANNOTATION_PROCESSOR.getModules().forEach((id, module) -> 
			module.initConfig(Type.SERVER, BUILDER)
		);
		
		BUILDER.pop();
		spec = BUILDER.build();
		
		ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.spec, "machina-server.toml");
    }

	@SuppressWarnings("unused")
	private static <T> ConfigValue<T> config(String comment, String path, T defaultValue) {
		return BUILDER.config(comment, path, defaultValue, true);
	}

}
