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

import java.util.function.Function;

import com.machina.api.planet.attribute.PlanetAttributeType;
import com.machina.api.registry.annotation.RegisterPlanetAttributeType;
import com.machina.api.registry.annotation.RegistryHolder;
import com.machina.api.util.Color;

import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;

@RegistryHolder
public final class PlanetAttributeTypesInit {

	@RegisterPlanetAttributeType("gravity")
	public static final PlanetAttributeType<Double> GRAVITY = new PlanetAttributeType<>("N", DoubleNBT::valueOf, nbt -> {
		if (nbt instanceof DoubleNBT) {
			return ((DoubleNBT) nbt).getAsDouble();
		}
		return 9.8;
	});
	
	@RegisterPlanetAttributeType("planet_name")
	public static final PlanetAttributeType<String> PLANET_NAME = new PlanetAttributeType<>("", StringNBT::valueOf, nbt -> {
		if (nbt instanceof StringNBT) {
			return ((StringNBT) nbt).getAsString();
		}
		return "Planet";
	});
	
	@RegisterPlanetAttributeType("colour")
	public static final PlanetAttributeType<Color> COLOUR = new PlanetAttributeType<>("", colour -> IntNBT.valueOf(colour.getRGB()), nbt -> {
		if (nbt instanceof IntNBT) {
			return new Color(((IntNBT) nbt).getAsInt());
		}
		return new Color(0);
	});
	
	@RegisterPlanetAttributeType("atmospheric_pressure")
	public static final PlanetAttributeType<Float> ATMOSPHERIC_PRESSURE = new PlanetAttributeType<>("Pa", FloatNBT::valueOf, floatDeserializer());
	
	@RegisterPlanetAttributeType("temperature")
	public static final PlanetAttributeType<Float> TEMPERATURE = new PlanetAttributeType<>("K", FloatNBT::valueOf, floatDeserializer());
	
	public static Function<INBT, Float> floatDeserializer() {
		return nbt -> {
			if (nbt instanceof FloatNBT) {
				return ((FloatNBT) nbt).getAsFloat();
			}
			return 1.0f;
		};
	}
}
