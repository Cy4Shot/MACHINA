package com.machina.world.gen;

import com.machina.world.gen.terrain.IslandsTerrainProcessor;
import com.machina.world.gen.terrain.NullTerrainProcessor;
import com.machina.world.gen.terrain.SurfaceTerrainProcessor;

public class PlanetTerrainGenerator {

	public interface IPlanetTerrainProcessor {
		
		String getKey();
		double postprocess(double height, double noise, double worldHeight, double surfmodifier);
		boolean hasBotBedrock();
		boolean hasTopBedrock();
		boolean isGas();
	}

	public static IPlanetTerrainProcessor getProcessor(int processor) {
		switch (processor) {
		case 0:
			return new SurfaceTerrainProcessor();
		case 1:
			return new IslandsTerrainProcessor();
		default:
			return new NullTerrainProcessor();
		}
	}
}