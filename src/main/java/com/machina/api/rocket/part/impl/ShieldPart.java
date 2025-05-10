package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.resources.ResourceLocation;

public class ShieldPart<T extends RocketPartModel> extends RocketPart<T> {

	private final float maxAtmDensity;

	public ShieldPart(ResourceLocation loc, Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model, float weight,
			float maxAtmDensity) {
		super(loc, RocketPartType.THRUSTER, upAnchor, downAnchor, model, weight);
		this.maxAtmDensity = maxAtmDensity;
	}

	public float getMaxAtmDensity() {
		return maxAtmDensity;
	}

}
