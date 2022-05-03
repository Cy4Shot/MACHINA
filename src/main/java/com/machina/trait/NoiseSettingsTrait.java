package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.DynamicDimensionChunkGenerator;
import com.machina.world.settings.DynamicNoiseSettings;

public class NoiseSettingsTrait extends PlanetTrait implements IWorldTrait {
	
	private final DynamicNoiseSettings settings;

	public NoiseSettingsTrait(int color, DynamicNoiseSettings settings) {
		super(color);
		
		this.settings = settings;
	}

	@Override
	public void init(DynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.noisesettings = this.settings;
	}

}
