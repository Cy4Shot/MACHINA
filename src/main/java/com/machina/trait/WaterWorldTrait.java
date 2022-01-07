package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;
import com.machina.api.world.DynamicDimensionChunkGenerator;

public class WaterWorldTrait extends PlanetTrait implements IWorldTrait {

	public WaterWorldTrait(int color, String description) {
		super(color, description);
	}
	
	@Override
	public void updateNoiseSettings(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.setSeaLevel(85);
	}

}
