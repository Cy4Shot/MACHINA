package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;
import com.machina.api.world.DynamicDimensionChunkGenerator;

public class HeightMultiplierTrait extends PlanetTrait implements IWorldTrait{
	final float height;

	public HeightMultiplierTrait(int color, String description, float h) {
		super(color, description);
		
		height = h;
	}
	
	@Override
	public void updateNoiseSettings(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.heightMultiplier = height;
	}
}
