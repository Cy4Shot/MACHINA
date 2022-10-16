package com.machina.world.gen.terrain;

import com.machina.world.gen.PlanetTerrainGenerator.IPlanetTerrainProcessor;

public class SurfaceTerrainProcessor implements IPlanetTerrainProcessor {

	@Override
	public double postprocess(double height, double noise, double worldHeight, double surfmodifier) {
		return noise - (height - worldHeight / 2) / (10 + surfmodifier * 90);
	}

	@Override
	public String getKey() {
		return "surface";
	}

	@Override
	public boolean hasBotBedrock() {
		return true;
	}

	@Override
	public boolean hasTopBedrock() {
		return false;
	}
}