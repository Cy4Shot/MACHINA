package com.machina.world.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class PlanetBiome extends Biome {

	static final ClimateSettings CLIMATE = new ClimateSettings(false, 1.0f, TemperatureModifier.NONE, 0f);
	static final BiomeSpecialEffects EFFECTS = new BiomeSpecialEffects.Builder().fogColor(0).skyColor(0).waterColor(0)
			.waterFogColor(0).build();
	static final BiomeGenerationSettings GEN = BiomeGenerationSettings.EMPTY;
	static final MobSpawnSettings MOB = MobSpawnSettings.EMPTY;

	public PlanetBiome() {
		super(CLIMATE, EFFECTS, GEN, MOB);
	}
}