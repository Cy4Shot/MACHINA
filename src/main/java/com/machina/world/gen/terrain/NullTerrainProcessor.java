package com.machina.world.gen.terrain;

import com.machina.world.gen.PlanetTerrainGenerator.IPlanetTerrainProcessor;

public class NullTerrainProcessor implements IPlanetTerrainProcessor {

	@Override
	public double postprocess(double height, double noise, double worldHeight, double surfmodifier) {
		return noise;
	}

	@Override
	public String getKey() {
		return "null";
	}
	
	@Override
	public boolean hasBotBedrock() {
		return false;
	}

	@Override
	public boolean hasTopBedrock() {
		return false;
	}

	@Override
	public boolean isGas() {
		return false;
	}
}