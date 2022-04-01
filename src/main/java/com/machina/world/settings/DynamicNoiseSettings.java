package com.machina.world.settings;

import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;

public class DynamicNoiseSettings extends NoiseSettings {

	//@formatter:off
	public static final DynamicNoiseSettings OVERWORLD_TYPE = new DynamicNoiseSettings(256,
			new ScalingSettings(0.9999999814507745, 0.9999999814507745, 80, 160),
			new SlideSettings(-10, 3, 0),
			new SlideSettings(-30, 0, 0),
			1, 2, 1D, -0.46875D, true, true, false);
	
	public static final DynamicNoiseSettings ISLAND_TYPE = new DynamicNoiseSettings(256,
			new ScalingSettings(1, 3, 80, 60),
			new SlideSettings(120, 3, 0),
			new SlideSettings(320, 4, -1),
			1, 2, 0D, 0.019921875D, false, false, false);
	//@formatter:on

	private final float noiseSizeX;
	private final float noiseSizeY;

	public float noiseSizeX() {
		return noiseSizeX;
	}

	public float noiseSizeY() {
		return noiseSizeY;
	}

	public DynamicNoiseSettings(int height, ScalingSettings noiseSamplingSettings, SlideSettings topSlideSettings,
			SlideSettings bottomSlideSettings, float noiseSizeX, float noiseSizeY, double densityFactor,
			double densityOffset, boolean simplexNoise, boolean randomDensity, boolean islandOverride) {
		super(height, noiseSamplingSettings, topSlideSettings, bottomSlideSettings, (int) noiseSizeX, (int) noiseSizeY,
				densityFactor, densityOffset, simplexNoise, randomDensity, islandOverride, false);

		this.noiseSizeX = noiseSizeX;
		this.noiseSizeY = noiseSizeY;
	}

}
