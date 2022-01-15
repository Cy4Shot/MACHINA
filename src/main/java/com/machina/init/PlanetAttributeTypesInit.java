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

package com.machina.init;

import static com.machina.api.ModIDs.MACHINA;

import java.util.Random;
import java.util.function.Function;

import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.registry.annotation.RegisterPlanetAttributeType;
import com.machina.api.util.Color;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetNameGenerator;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;

@RegistryHolder(modid = MACHINA)
public final class PlanetAttributeTypesInit {
	
	@RegisterPlanetAttributeType("distance")
	public static final PlanetAttributeType<Float> DISTANCE = new PlanetAttributeType<>("AU", FloatNBT::valueOf, floatDeserializer(1.0f), random(0.5f, 10f));

	@RegisterPlanetAttributeType("gravity")
	public static final PlanetAttributeType<Float> GRAVITY = new PlanetAttributeType<>("N", FloatNBT::valueOf, floatDeserializer(9.8f), random(5.0f, 15.0f));
	
	@RegisterPlanetAttributeType("planet_name")
	public static final PlanetAttributeType<String> PLANET_NAME = new PlanetAttributeType<>("", StringNBT::valueOf, stringDeserializer("Planet"), PlanetNameGenerator::getName);
	
	@RegisterPlanetAttributeType("fog_density")
	public static final PlanetAttributeType<Float> FOG_DENSITY = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.5f), random(0f, 1.0f));
	
	@RegisterPlanetAttributeType("fog_colour")
	public static final PlanetAttributeType<Color> FOG_COLOUR = new PlanetAttributeType<>("", colorSerializer(), colorDeserializer(new Color(0)), Color::random);
	
	@RegisterPlanetAttributeType("primary_colour")
	public static final PlanetAttributeType<Color> PRIMARY_COLOUR = new PlanetAttributeType<>("", colorSerializer(), colorDeserializer(new Color(0)), Color::random);
	
	@RegisterPlanetAttributeType("atmospheric_pressure")
	public static final PlanetAttributeType<Float> ATMOSPHERIC_PRESSURE = new PlanetAttributeType<>("Pa", FloatNBT::valueOf, floatDeserializer(1.0f), random(0.1f, 2.0f));
	
	@RegisterPlanetAttributeType("temperature")
	public static final PlanetAttributeType<Float> TEMPERATURE = new PlanetAttributeType<>("K", FloatNBT::valueOf, floatDeserializer(350.0f), random(100f, 1000f));
	
	@RegisterPlanetAttributeType("base_block")
	public static final PlanetAttributeType<String> BASE_BLOCK = new PlanetAttributeType<>("", StringNBT::valueOf, stringDeserializer("minecraft:stone"), PlanetBlocksGenerator::getBaseBlock);
	
	@RegisterPlanetAttributeType("caves_exist")
	public static final PlanetAttributeType<Integer> CAVES_EXIST = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(0), random(0, 1));
	
	@RegisterPlanetAttributeType("cave_chance")
	public static final PlanetAttributeType<Float> CAVE_CHANCE = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.01f), random(0f, 0.02f));
	
	@RegisterPlanetAttributeType("cave_length")
	public static final PlanetAttributeType<Integer> CAVE_LENGTH = new PlanetAttributeType<>("", IntNBT::valueOf, intDeserializer(3), random(1, 5));
	
	@RegisterPlanetAttributeType("cave_thickness")
	public static final PlanetAttributeType<Float> CAVE_THICKNESS = new PlanetAttributeType<>("", FloatNBT::valueOf, floatDeserializer(0.01f), random(0f, 0.02f, 1.7f));
	
	
	
	
	// Deserializers
	public static Function<INBT, Float> floatDeserializer(float defaultVal) {
		return nbt -> {
			if (nbt instanceof FloatNBT) {
				return ((FloatNBT) nbt).getAsFloat();
			}
			return defaultVal;
		};
	}
	
	// Deserializers
	public static Function<INBT, Integer> intDeserializer(int defaultVal) {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return ((IntNBT) nbt).getAsInt();
			}
			return defaultVal;
		};
	}
	
	public static Function<INBT, String> stringDeserializer(String defaultVal) {
		return nbt -> {
			if (nbt instanceof StringNBT) {
				return ((StringNBT) nbt).getAsString();
			}
			return defaultVal;
		};
	}
	
	public static Function<INBT, Color> colorDeserializer(Color defaultVal) {
		return nbt -> {
			if (nbt instanceof IntNBT) {
				return new Color(((IntNBT) nbt).getAsInt());
			}
			return defaultVal;
		};
	}
	
	// Serializers
	public static Function<Color, INBT> colorSerializer() {
		return color -> IntNBT.valueOf(color.getRGB());
	}
	
	// Random
	public static Function<Random, Float> random(float min, float max) {
		return r -> min + r.nextFloat() * (max - min);
	}
	
	public static Function<Random, Float> random(float min, float max, float exp) {
		return r -> min + (float) Math.pow(r.nextFloat(), exp) * (max - min);
	}
	
	public static Function<Random, Integer> random(int min, int max) {
		return r -> r.nextInt((max - min) + 1) + min;
	}
}
