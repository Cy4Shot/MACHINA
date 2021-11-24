package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterPlanetAttributeType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.planet.attribute.PlanetAttributeType;

import net.minecraft.nbt.DoubleNBT;

@RegistryHolder
public class PlanetAttributeTypesInit {

	@RegisterPlanetAttributeType("gravity")
	public static final PlanetAttributeType<Double> GRAVITY = new PlanetAttributeType<>("G", DoubleNBT::valueOf, nbt -> {
		if (nbt instanceof DoubleNBT) {
			return ((DoubleNBT) nbt).getAsDouble();
		}
		return 9.8;
	});
	
}
