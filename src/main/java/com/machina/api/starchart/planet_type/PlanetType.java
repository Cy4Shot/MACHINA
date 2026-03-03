package com.machina.api.starchart.planet_type;

import java.util.List;
import java.util.function.Function;

import com.machina.weather.WeatherEvent;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

public record PlanetType(ResourceLocation name, int iconY, Shape shape, List<BiomePlacement> biomes, List<WeatherEvent> weathers, BlockState base) {

	public static final Codec<PlanetType> CODEC = RecordCodecBuilder
			.create(instance -> instance
					.group(ResourceLocation.CODEC.fieldOf("name").forGetter(PlanetType::name),
							Codec.INT.fieldOf("iconY").forGetter(PlanetType::iconY),
							Shape.CODEC.fieldOf("shape").forGetter(PlanetType::shape),
							Codec.list(BiomePlacement.CODEC).fieldOf("biomes").forGetter(PlanetType::biomes),
							Codec.list(WeatherEvent.CODEC).fieldOf("weathers").forGetter(PlanetType::weathers),
							BlockState.CODEC.fieldOf("base").forGetter(PlanetType::base))
					.apply(instance, PlanetType::new));

	public record Shape(int sea_level, NoiseSettings noise_settings) {
		public static final Codec<Shape> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("sea_level").forGetter(Shape::sea_level),
						NoiseSettings.CODEC.fieldOf("noise_settings").forGetter(Shape::noise_settings))
				.apply(instance, Shape::new));

		public net.minecraft.world.level.levelgen.NoiseSettings getNoiseSettings() {
			return new net.minecraft.world.level.levelgen.NoiseSettings(noise_settings.minY(), noise_settings.height(),
					noise_settings.noiseSizeHorizontal(), noise_settings.noiseSizeVertical());
		}
	}

	public record BiomePlacement(ResourceLocation biome, List<String> placements) {
		public static final Codec<BiomePlacement> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(ResourceLocation.CODEC.fieldOf("biome").forGetter(BiomePlacement::biome),
						Codec.list(Codec.STRING).fieldOf("placements").forGetter(BiomePlacement::placements))
				.apply(instance, BiomePlacement::new));
	}

	public record NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {

		public static final Codec<NoiseSettings> CODEC = RecordCodecBuilder.<NoiseSettings>create((x) -> {
			return x.group(
					Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("min_y")
							.forGetter(NoiseSettings::minY),
					Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(NoiseSettings::height),
					Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::noiseSizeHorizontal),
					Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::noiseSizeVertical))
					.apply(x, NoiseSettings::new);
		}).comapFlatMap(NoiseSettings::guardY, Function.identity());

		private static DataResult<NoiseSettings> guardY(NoiseSettings p_158721_) {
			if (p_158721_.minY() + p_158721_.height() > DimensionType.MAX_Y + 1) {
				return DataResult.error(() -> {
					return "min_y + height cannot be higher than: " + (DimensionType.MAX_Y + 1);
				});
			} else if (p_158721_.height() % 16 != 0) {
				return DataResult.error(() -> {
					return "height has to be a multiple of 16";
				});
			} else {
				return p_158721_.minY() % 16 != 0 ? DataResult.error(() -> {
					return "min_y has to be a multiple of 16";
				}) : DataResult.success(p_158721_);
			}
		}
	}
}
