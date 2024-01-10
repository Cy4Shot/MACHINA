package com.machina.client.model.celestial;

import com.machina.api.util.MachinaRL;

import net.minecraft.resources.ResourceLocation;

public class PlanetModel extends CelestialModel {

	@Override
	public void setup() {
//		setupCube();
		setupSphere(25);
	}

	@Override
	public ResourceLocation tex() {
		return new MachinaRL("gui/starchart/planet");
	}
}