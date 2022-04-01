package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.DynamicDimensionChunkGenerator;

public class HeightMultiplierTrait extends PlanetTrait implements IWorldTrait{
	final float height;

	public HeightMultiplierTrait(int color, float h) {
		super(color);
		
		height = h;
	}
	
	@Override
	public void init(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.heightMultiplier = height;
	}
}
