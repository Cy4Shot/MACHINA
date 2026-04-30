package com.machina.api.starchart.planet_type;

import java.util.List;
import java.util.stream.Collectors;

import com.machina.api.fluid.ChemicalFluid;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.starchart.planet_type.PlanetType.BiomePlacement;
import com.machina.api.starchart.planet_type.PlanetType.PlanetOre;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettings;
import com.machina.api.starchart.planet_type.PlanetType.PlanetTraitSettingsEntry;
import com.machina.api.starchart.planet_type.PlanetType.Shape;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.loader.JsonInfo;
import com.machina.registration.init.RegistryInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetTypeJsonInfo(String name, int iconY, int color, int shaderId, Shape shape,
		List<BiomePlacementJsonInfo> biomes, List<String> weathers, PlanetTraitSettingsJsonInfo traits, String base,
		List<PlanetOreJsonInfo> ores, String dominant_liquid) implements JsonInfo<PlanetType> {

	public static BlockState getBlock(String block) {
		return BlockHelper.parseState(BlockHelper.blockHolderLookup(), block);
	}

	public record BiomePlacementJsonInfo(String name, List<String> placements) implements JsonInfo<BiomePlacement> {

		@Override
		public BiomePlacement cast() {
			ResourceLocation biome = ResourceLocation.parse(name());
			return new BiomePlacement(biome, placements());
		}
	}

	public record PlanetTraitSettingsJsonInfo(int minRolls, int maxRolls,
			List<PlanetTraitSettingsEntryJsonInfo> weights) implements JsonInfo<PlanetTraitSettings> {

		@Override
		public PlanetTraitSettings cast() {
			List<PlanetTraitSettingsEntry> entries = weights.stream().map(PlanetTraitSettingsEntryJsonInfo::cast)
					.collect(Collectors.toList());
			return new PlanetTraitSettings(minRolls, maxRolls, entries);
		}
	}

	public record PlanetTraitSettingsEntryJsonInfo(String trait, float weight)
			implements JsonInfo<PlanetTraitSettingsEntry> {

		@Override
		public PlanetTraitSettingsEntry cast() {
			PlanetTrait planetTrait = RegistryInit.TRAIT.get(ResourceLocation.parse(trait));
			return new PlanetTraitSettingsEntry(planetTrait, weight);
		}
	}

	public record PlanetOreJsonInfo(String ore, String block, int size, Float exposure_removal_chance, float chance,
			int min_y, int max_y) implements JsonInfo<PlanetOre> {

		@Override
		public PlanetOre cast() {
			float exposure = exposure_removal_chance == null ? 0.0f : exposure_removal_chance;

			if (ore != null) {
				return PlanetOre.fromOre(ResourceLocation.parse(ore), size, exposure, chance, min_y, max_y);
			}

			return PlanetOre.fromBlock(getBlock(block), size, exposure, chance, min_y, max_y);
		}
	}

	@Override
	public PlanetType cast() {
		ResourceLocation name = ResourceLocation.parse(name());
		List<BiomePlacement> biomes = biomes().stream().map(BiomePlacementJsonInfo::cast).collect(Collectors.toList());
		List<WeatherEvent> weathers = weathers().stream()
				.map(x -> RegistryInit.WEATHER_EVENT.get(ResourceLocation.parse(x))).collect(Collectors.toList());
		List<PlanetOre> ores = ores().stream().map(PlanetOreJsonInfo::cast).collect(Collectors.toList());

		HolderLookup<Block> block = BlockHelper.blockHolderLookup();
		BlockState base = BlockHelper.parseState(block, base());
		ChemicalFluid dominant_liquid = dominant_liquid() == null ? null
				: BlockHelper.parseChemicalFluid(dominant_liquid());
		return new PlanetType(name, iconY, color, shaderId, shape(), biomes, weathers, traits.cast(), base, ores,
				dominant_liquid);
	}
}