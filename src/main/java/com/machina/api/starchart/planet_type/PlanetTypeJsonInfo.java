package com.machina.api.starchart.planet_type;

import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.starchart.planet_type.PlanetType.BiomePlacement;
import com.machina.api.starchart.planet_type.PlanetType.Shape;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.loader.JsonInfo;
import com.machina.registration.init.RegistryInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetTypeJsonInfo(String name, int iconY, int color, Shape shape, List<BiomePlacementJsonInfo> biomes,
		List<String> weathers, String base) implements JsonInfo<PlanetType> {

	public record BiomePlacementJsonInfo(String name, List<String> placements) implements JsonInfo<BiomePlacement> {

		@Override
		public BiomePlacement cast() {
			ResourceLocation biome = ResourceLocation.parse(name());
			return new BiomePlacement(biome, placements());
		}
	}

	@Override
	public PlanetType cast() {
		ResourceLocation name = ResourceLocation.parse(name());
		List<BiomePlacement> biomes = biomes().stream().map(BiomePlacementJsonInfo::cast).collect(Collectors.toList());
		List<WeatherEvent> weathers = weathers().stream()
				.map(x -> RegistryInit.WEATHER_EVENT.get(ResourceLocation.parse(x))).collect(Collectors.toList());

		HolderLookup<Block> block = BlockHelper.blockHolderLookup();
		BlockState base = BlockHelper.parseState(block, base());
		return new PlanetType(name, iconY, color, shape(), biomes, weathers, base);
	}
}