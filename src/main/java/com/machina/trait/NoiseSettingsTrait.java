package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;
import com.machina.world.PlanetChunkGenerator;
import com.machina.world.settings.PlanetNoiseSettings;

public class NoiseSettingsTrait extends PlanetTrait implements IWorldTrait {
	
	private final PlanetNoiseSettings settings;

	public NoiseSettingsTrait(int color, PlanetNoiseSettings settings) {
		super(color);
		
		this.settings = settings;
	}

	@Override
	public void init(PlanetChunkGenerator chunkGenerator) {
		chunkGenerator.noisesettings = this.settings;
	}

}
