package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.DynamicDimensionChunkGenerator;
import com.machina.world.settings.DynamicNoiseSettings;

public class IslandsTrait extends PlanetTrait implements IWorldTrait {

	public IslandsTrait(int color) {
		super(color);
	}

	@Override
	public void init(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.noisesettings = DynamicNoiseSettings.ISLAND_TYPE;
		chunkGenerator.islands = true;
	}

}
