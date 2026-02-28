package com.machina.api.rocket.part.impl;

import java.util.function.Supplier;

import com.machina.api.rocket.part.RocketPart;
import com.machina.api.rocket.part.RocketPartType;

import net.minecraft.resources.ResourceLocation;

public class LifeSupportPart extends RocketPart {

	private final int slots;

	public LifeSupportPart(ResourceLocation loc, float height, Supplier<?> model, float weight, float off,
			float guiScale, int slots) {
		super(loc, RocketPartType.LIFE_SUPPORT, height, off, guiScale, model, weight);
		this.slots = slots;
	}

	public int getSlots() {
		return slots;
	}

}
