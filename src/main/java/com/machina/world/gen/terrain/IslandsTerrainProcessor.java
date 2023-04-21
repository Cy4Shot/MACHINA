package com.machina.world.gen.terrain;

import com.machina.world.gen.PlanetTerrainGenerator.IPlanetTerrainProcessor;

public class IslandsTerrainProcessor implements IPlanetTerrainProcessor {

	@Override
	public double postprocess(double height, double noise, double worldHeight, double surfmodifier) {
		return noise - Math.abs(height - worldHeight / 2) / (30 + surfmodifier * 50);
	}

	@Override
	public String getKey() {
		return "islands";
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
		return true;
	}
}