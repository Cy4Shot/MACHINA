package com.machina.api.rocket.part.impl;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;

import net.minecraft.resources.ResourceLocation;

public class FuelTankPart extends RocketPart {

	private final int fuelStorage;
	private final int coolantStorage;

	public FuelTankPart(ResourceLocation loc, float height, float weight, float off, float guiScale,
			int fuelStorage, int coolantStorage) {
		super(loc, RocketPartType.FUEL_TANK, height, off, guiScale, weight);
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
