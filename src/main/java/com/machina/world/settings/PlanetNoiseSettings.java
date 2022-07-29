package com.machina.world.settings;

import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;

public class PlanetNoiseSettings extends NoiseSettings {

	//@formatter:off
	public static final PlanetNoiseSettings OVERWORLD_TYPE = new PlanetNoiseSettings(256,
			new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80D, 160D),
			new SlideSettings(-10, 3, 0),
			new SlideSettings(-30, 0, 0),
			1F, 2F, 1D, -0.46875D, true, true, false);
	
	public static final PlanetNoiseSettings LAYERED_TYPE = new PlanetNoiseSettings(256,
			new ScalingSettings(1D, 3D, 80D, 60D),
			new SlideSettings(120, 3, 0),
			new SlideSettings(320, 4, -1),
			1F, 2F, 0D, 0.019921875D, false, false, false);
	
	public static final PlanetNoiseSettings ISLAND_TYPE = new PlanetNoiseSettings(256,
			new ScalingSettings(1D, 3D, 80D, 60D),
			new SlideSettings(-120, 3, 0),
			new SlideSettings(-30, 3, 2),
			1F, 2F, 0D, 0.019921875D, false, false, false);
	//@formatter:on

	private final float noiseSizeX;
	private final float noiseSizeY;

	public float noiseSizeX() {
		return noiseSizeX;
	}

	public float noiseSizeY() {
		return noiseSizeY;
	}

	public PlanetNoiseSettings(int height, ScalingSettings noiseSamplingSettings, SlideSettings topSlideSettings,
			SlideSettings bottomSlideSettings, float noiseSizeX, float noiseSizeY, double densityFactor,
			double densityOffset, boolean simplexNoise, boolean randomDensity, boolean islandOverride) {
		super(height, noiseSamplingSettings, topSlideSettings, bottomSlideSettings, (int) noiseSizeX, (int) noiseSizeY,
				densityFactor, densityOffset, simplexNoise, randomDensity, islandOverride, false);

		this.noiseSizeX = noiseSizeX;
		this.noiseSizeY = noiseSizeY;
	}

}
