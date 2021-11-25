/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import java.util.function.Function;

import com.cy4.machina.api.planet.attribute.PlanetAttributeType;
import com.cy4.machina.api.registry.annotation.RegisterPlanetAttributeType;
import com.cy4.machina.api.registry.annotation.RegistryHolder;
import com.cy4.machina.api.util.Color;

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
