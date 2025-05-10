package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.client.rocket.model.RocketPartModel;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.resources.ResourceLocation;

public class ThrusterPart<T extends RocketPartModel> extends RocketPart<T> {

	private final FluidObject fuel;
	private final float fuelEfficiency;

	public ThrusterPart(ResourceLocation loc, Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model, float weight, FluidObject fuel,
			float fuelEfficiency) {
		super(loc, RocketPartType.THRUSTER, upAnchor, downAnchor, model, weight);
		this.fuel = fuel;
		this.fuelEfficiency = fuelEfficiency;
	}

	public FluidObject getFuel() {
		return fuel;
	}

	public float getFuelEfficiency() {
		return fuelEfficiency;
	}

}
