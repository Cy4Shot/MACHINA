package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.PlanetChunkGenerator;

public class HeightMultiplierTrait extends PlanetTrait implements IWorldTrait{
	final float height;

	public HeightMultiplierTrait(int color, float h) {
		super(color);
		
		height = h;
	}
	
	@Override
	public void init(PlanetChunkGenerator chunkGenerator) {
		chunkGenerator.heightMultiplier = height;
	}
}
