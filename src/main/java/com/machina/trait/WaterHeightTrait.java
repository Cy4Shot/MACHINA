package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.DynamicDimensionChunkGenerator;

public class WaterHeightTrait extends PlanetTrait implements IWorldTrait {
	
	final int height;

	public WaterHeightTrait(int color,int h) {
		super(color);
		
		height = h;
	}
	
	@Override
	public void init(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.seaLevel = height;
	}

}
