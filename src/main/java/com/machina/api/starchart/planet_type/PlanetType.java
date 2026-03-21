package com.machina.api.starchart.planet_type;

import java.util.List;

import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.weather.WeatherEvent;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetType(ResourceLocation name, int iconY, int color, int shaderId, Shape shape,
		List<BiomePlacement> biomes, List<WeatherEvent> weathers, PlanetTraitSettings traits, BlockState base) {

	public record Shape(int sea_level, NoiseSettings noise_settings) {
		public net.minecraft.world.level.levelgen.NoiseSettings getNoiseSettings() {
			return new net.minecraft.world.level.levelgen.NoiseSettings(noise_settings.minY(), noise_settings.height(),
					noise_settings.noiseSizeHorizontal(), noise_settings.noiseSizeVertical());
		}
	}

	public record BiomePlacement(ResourceLocation biome, List<String> placements) {
	}

	public record NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
	}

	public record PlanetTraitSettings(int minRolls, int maxRolls, List<PlanetTraitSettingsEntry> weights) {
	}

	public record PlanetTraitSettingsEntry(PlanetTrait trait, float weight) {
	}

	public MutableComponent nameComp() {
		return Component.translatable(name.getNamespace() + ".planet_type." + name.getPath());
	}
}
