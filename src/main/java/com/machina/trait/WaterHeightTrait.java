package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;
import com.machina.api.world.DynamicDimensionChunkGenerator;

public class WaterHeightTrait extends PlanetTrait implements IWorldTrait {
	
	final int height;

	public WaterHeightTrait(int color, String description, int h) {
		super(color, description);
		
		height = h;
	}
	
	@Override
	public void updateNoiseSettings(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.seaLevel = height;
	}

}
