package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.client.rocket.model.RocketPartModel;

import net.minecraft.resources.ResourceLocation;

public class FuelTankPart<T extends RocketPartModel> extends RocketPart<T> {

	private final int fuelStorage;
	private final int coolantStorage;

	public FuelTankPart(ResourceLocation loc, Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model, float weight,
			int fuelStorage, int coolantStorage) {
		super(loc, RocketPartType.THRUSTER, upAnchor, downAnchor, model, weight);
		this.fuelStorage = fuelStorage;
		this.coolantStorage = coolantStorage;
	}

	public int getFuelStorage() {
		return fuelStorage;
	}

	public int getCoolantStorage() {
		return coolantStorage;
	}

}
