package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.PlanetChunkGenerator;

public class FrozenTrait extends PlanetTrait implements IWorldTrait {

	public FrozenTrait(int color) {
		super(color);
	}

	@Override
	public void init(PlanetChunkGenerator chunkGenerator) {
		chunkGenerator.frozen = true;
	}
}