package com.machina.api.starchart.planet_biome;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBush;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeGrass;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeLakes;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBlockWeight;
import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.machina.api.starchart.planet_biome.placement.PlacementModifierType;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.loader.JsonInfo;
import com.machina.registration.init.RegistryInit;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetBiomeJsonInfo(String home, String base, String surface, List<String> top, String second,
		String stair, String slab, String extra, List<PlanetBiomeTreeJsonInfo> trees,
		List<PlanetBiomeBushJsonInfo> bushes, PlanetBiomeGrassJsonInfo grass, PlanetBiomeLakesJsonInfo lakes,
		List<PlanetBiomeRockJsonInfo> rocks, List<PlanetBiomeBigRockJsonInfo> big_rocks)
		implements JsonInfo<PlanetBiomeSettings> {

	public static BlockState getBlock(String block) {
		return BlockHelper.parseState(BlockHelper.blockHolderLookup(), block);
	}

	public record PlanetBiomeTreeJsonInfo(String type, List<String> blocks, float chance, List<String> fruits,
			List<String> fruit_dirs, float fruit_chance, float tree_fruit_chance) implements JsonInfo<PlanetBiomeTree> {

		@Override
		public PlanetBiomeTree cast() {
			List<BlockState> blocks = blocks().stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> fruits = fruits().stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<Direction> fruit_dirs = fruit_dirs().stream().map(Direction::valueOf).collect(Collectors.toList());
			return new PlanetBiomeTree(ResourceLocation.parse(type), blocks, chance, fruits, fruit_dirs, fruit_chance,
					tree_fruit_chance);
		}
	}

	public record PlanetBiomeBushJsonInfo(String block, float radius, int perchunk)
			implements JsonInfo<PlanetBiomeBush> {
		@Override
		public PlanetBiomeBush cast() {
			return new PlanetBiomeBush(getBlock(block), radius, perchunk);
		}
	}

	public record PlanetBiomeGrassJsonInfo(boolean enabled, int min, int max, List<PlanetBlockWeightJsonInfo> grasses)
			implements JsonInfo<PlanetBiomeGrass> {
		@Override
		public PlanetBiomeGrass cast() {
			List<PlanetBlockWeight> grasses = grasses().stream().map(PlanetBlockWeightJsonInfo::cast)
					.collect(Collectors.toList());
			return new PlanetBiomeGrass(enabled, min, max, grasses);
		}
	}

	public record PlanetBiomeLakesJsonInfo(String block, boolean enabled, float chance, float decorator_chance,
			List<PlanetBlockWeightJsonInfo> decorators) implements JsonInfo<PlanetBiomeLakes> {
		@Override
		public PlanetBiomeLakes cast() {
			List<PlanetBlockWeight> decorators = decorators().stream().map(PlanetBlockWeightJsonInfo::cast)
					.collect(Collectors.toList());
			return new PlanetBiomeLakes(getBlock(block), enabled, chance, decorator_chance, decorators);
		}
	}

	public record PlanetBiomeRockJsonInfo(String base, String stair, String slab, String wall, float chance,
			float radius, float deform, List<PlacementModifierJsonInfo> placement_modifiers)
			implements JsonInfo<PlanetBiomeRock> {
		@Override
		public PlanetBiomeRock cast() {
			List<PlacementModifier> modifiers = placement_modifiers == null ? List.of()
					: placement_modifiers.stream().map(PlacementModifierJsonInfo::cast).toList();
			return new PlanetBiomeRock(getBlock(base), getBlock(stair), getBlock(slab), getBlock(wall), chance, radius,
					deform, modifiers);
		}
	}

	public record PlanetBiomeBigRockJsonInfo(String type, String block, String extra, float chance,
			float up_extra_chance, float down_extra_chance, float side_extra_chance,
			List<PlacementModifierJsonInfo> placement_modifiers) implements JsonInfo<PlanetBiomeBigRock> {

		@Override
		public PlanetBiomeBigRock cast() {
			List<PlacementModifier> modifiers = placement_modifiers == null ? List.of()
					: placement_modifiers.stream().map(PlacementModifierJsonInfo::cast).toList();
			return new PlanetBiomeBigRock(ResourceLocation.parse(type), getBlock(block), getBlock(extra), chance,
					up_extra_chance, down_extra_chance, side_extra_chance, modifiers);
		}
	}

	public record PlanetBlockWeightJsonInfo(String block, int weight) implements JsonInfo<PlanetBlockWeight> {
		@Override
		public PlanetBlockWeight cast() {
			return new PlanetBlockWeight(getBlock(block), weight);
		}
	}

	public record PlacementModifierJsonInfo(String type, JsonObject params) implements JsonInfo<PlacementModifier> {
		public PlacementModifier cast() {
			ResourceLocation id = ResourceLocation.parse(type);
			PlacementModifierType modifierType = RegistryInit.PLACEMENT_MODIFIER.get(id);
			if (modifierType == null) {
				throw new IllegalArgumentException("Unknown modifier: " + type);
			}
			return modifierType.create(params);
		}
	}

	@Override
	public PlanetBiomeSettings cast() {
		List<BlockState> tops = top.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
		List<PlanetBiomeTree> trees = trees().stream().map(PlanetBiomeTreeJsonInfo::cast).collect(Collectors.toList());
		List<PlanetBiomeBush> bushes = bushes().stream().map(PlanetBiomeBushJsonInfo::cast)
				.collect(Collectors.toList());
		PlanetBiomeGrass grass = grass().cast();
		PlanetBiomeLakes lakes = lakes().cast();
		List<PlanetBiomeRock> rocks = rocks().stream().map(PlanetBiomeRockJsonInfo::cast).collect(Collectors.toList());
		List<PlanetBiomeBigRock> big_rocks = big_rocks().stream().map(PlanetBiomeBigRockJsonInfo::cast)
				.collect(Collectors.toList());

		return new PlanetBiomeSettings(ResourceLocation.parse(home), getBlock(base), ResourceLocation.parse(surface),
				tops, getBlock(second), getBlock(stair), getBlock(slab), getBlock(extra), trees, bushes, grass, lakes,
				rocks, big_rocks);
	}
}