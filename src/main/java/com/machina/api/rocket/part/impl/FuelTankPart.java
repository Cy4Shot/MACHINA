package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.client.rocket.model.RocketPartModel;

public class FuelTankPart<T extends RocketPartModel> extends RocketPart<T> {

	private final float fuelStorage;
	private final float coolantStorage;

	public FuelTankPart(Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model, float weight, float fuelStorage,
			float coolantStorage) {
		super(RocketPartType.THRUSTER, upAnchor, downAnchor, model, weight);
		this.fuelStorage = fuelStorage;
		this.coolantStorage = coolantStorage;
	}

	public float getFuelStorage() {
		return fuelStorage;
	}

	public float getCoolantStorage() {
		return coolantStorage;
	}

}
