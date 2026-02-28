package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;

import net.minecraft.resources.ResourceLocation;

public class ShieldPart extends RocketPart {

	private final float maxAtmPressure;

	public ShieldPart(ResourceLocation loc, float height, Supplier<?> model, float weight, float off, float guiScale,
			float maxAtmPressure) {
		super(loc, RocketPartType.SHIELD, height, off, guiScale, model, weight);
		this.maxAtmPressure = maxAtmPressure;
	}

	public float getMaxAtmPressure() {
		return maxAtmPressure;
	}

}
