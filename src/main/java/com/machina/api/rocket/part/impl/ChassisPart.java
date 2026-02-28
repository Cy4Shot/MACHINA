package com.machina.api.rocket.part.impl;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;
import com.machina.registration.init.FluidInit.FluidObject;

import net.minecraft.resources.ResourceLocation;

public class ChassisPart extends RocketPart {

	private final FluidObject coolant;
	private final float coolantEfficiency;

	public ChassisPart(ResourceLocation loc, float height, float weight, float off, float guiScale,
			FluidObject coolant, float coolantEfficiency) {
		super(loc, RocketPartType.CHASSIS, height, off, guiScale, weight);
		this.coolant = coolant;
		this.coolantEfficiency = coolantEfficiency;
	}

	public FluidObject getCoolant() {
		return coolant;
	}

	public float getCoolantEfficiency() {
		return coolantEfficiency;
	}

}
