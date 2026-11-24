package com.machina.api.starchart.planet_biome;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBush;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeGrass;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeLakes;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRock;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRuin;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityGrid;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityOvergrown;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeOvergrownGround;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeOvergrownVine;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityUrban;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeCityVault;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBlocks;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBlockWeight;
import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.machina.api.starchart.planet_biome.placement.PlacementModifierType;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetHightechRuinShape;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.loader.JsonInfo;
import com.machina.registration.init.RegistryInit;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetBiomeJsonInfo(String home, String base, String surface, List<String> top, String second,
		String stair, String slab, String extra, List<PlanetBiomeTreeJsonInfo> trees,
		List<PlanetBiomeBushJsonInfo> bushes, PlanetBiomeGrassJsonInfo grass, PlanetBiomeLakesJsonInfo lakes,
		List<PlanetBiomeRockJsonInfo> rocks, List<PlanetBiomeBigRockJsonInfo> big_rocks,
		PlanetBiomeCityGridJsonInfo city_grid, PlanetBiomeClientSettingsJsonInfo client_settings)
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

	public record PlanetBiomeRuinJsonInfo(String shape, int min_size, int max_size, int min_height, int max_height,
			float chance, float min_weathering, float max_weathering)
			implements JsonInfo<PlanetBiomeRuin> {
		public PlanetBiomeRuin cast() {
			return new PlanetBiomeRuin(PlanetHightechRuinShape.valueOf(this.shape), min_size, max_size, min_height,
					max_height, chance, min_weathering, max_weathering);
		}
	}

	public record PlanetBiomeOvergrownGroundJsonInfo(
			List<String> base_blocks, List<String> lichen_blocks, List<String> shroom_blocks,
			Float floor_chance, Integer cluster_min_count, Integer cluster_max_count,
			Integer cluster_min_size, Integer cluster_max_size, Float lichen_chance, Float shroom_chance,
			Float dirt_floor_chance, List<String> dirt_floor_blocks, List<String> dirt_floor_grass) implements JsonInfo<PlanetBiomeOvergrownGround> {
		public PlanetBiomeOvergrownGround cast() {
			List<BlockState> baseBlocks = base_blocks == null ? List.of()
					: base_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> lichenBlocks = lichen_blocks == null ? List.of()
					: lichen_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> shroomBlocks = shroom_blocks == null ? List.of()
					: shroom_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> dirtFloorBlocks = dirt_floor_blocks == null ? List.of()
					: dirt_floor_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> dirtFloorGrass = dirt_floor_grass == null ? List.of()
					: dirt_floor_grass.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			return new PlanetBiomeOvergrownGround(baseBlocks, lichenBlocks, shroomBlocks,
					floor_chance != null ? floor_chance : 0.6f,
					cluster_min_count != null ? cluster_min_count : 1,
					cluster_max_count != null ? cluster_max_count : 3,
					cluster_min_size != null ? cluster_min_size : 2,
					cluster_max_size != null ? cluster_max_size : 4,
					lichen_chance != null ? lichen_chance : 0.4f,
					shroom_chance != null ? shroom_chance : 0.15f,
					dirt_floor_chance != null ? dirt_floor_chance : 0f,
					dirtFloorBlocks,
					dirtFloorGrass);
		}
	}

	public record PlanetBiomeOvergrownVineJsonInfo(
			List<String> wall_blocks, Float wall_chance, Integer min_wall_height, Integer max_wall_height,
			Float vine_chance, Integer vine_min_length, Integer vine_max_length,
			Integer vine_min_branches, Integer vine_max_branches, Integer vine_thickness) implements JsonInfo<PlanetBiomeOvergrownVine> {
		public PlanetBiomeOvergrownVine cast() {
			List<BlockState> wallBlocks = wall_blocks == null ? List.of()
					: wall_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			return new PlanetBiomeOvergrownVine(wallBlocks,
					wall_chance != null ? wall_chance : 0.3f,
					min_wall_height != null ? min_wall_height : 10,
					max_wall_height != null ? max_wall_height : 70,
					vine_chance != null ? vine_chance : 0.85f,
					vine_min_length != null ? vine_min_length : 20,
					vine_max_length != null ? vine_max_length : 50,
					vine_min_branches != null ? vine_min_branches : 2,
					vine_max_branches != null ? vine_max_branches : 4,
					vine_thickness != null ? vine_thickness : 2);
		}
	}

	public record PlanetBiomeCityOvergrownJsonInfo(boolean enabled,
			PlanetBiomeOvergrownGroundJsonInfo ground, PlanetBiomeOvergrownVineJsonInfo vines) implements JsonInfo<PlanetBiomeCityOvergrown> {
		public PlanetBiomeCityOvergrown cast() {
			PlanetBiomeOvergrownGround groundConfig = ground == null ? PlanetBiomeOvergrownGround.DISABLED : ground.cast();
			PlanetBiomeOvergrownVine vinesConfig = vines == null ? PlanetBiomeOvergrownVine.DISABLED : vines.cast();
			return new PlanetBiomeCityOvergrown(enabled, groundConfig, vinesConfig);
		}
	}

	public record PlanetBiomeCityUrbanJsonInfo(Integer suburb_min_buildings, Integer suburb_max_buildings,
			Integer center_min_buildings, Integer center_max_buildings,
			Float suburb_height_mult, Float center_height_mult,
			Integer noise_scale, Float toxic_garden_chance, Integer road_buffer,
			Float garden_tree_chance, List<PlanetBiomeTreeJsonInfo> garden_trees) implements JsonInfo<PlanetBiomeCityUrban> {
		public PlanetBiomeCityUrban cast() {
			List<PlanetBiomeTree> trees = garden_trees == null ? List.of()
					: garden_trees.stream().map(PlanetBiomeTreeJsonInfo::cast).collect(Collectors.toList());
			return new PlanetBiomeCityUrban(
					suburb_min_buildings != null ? suburb_min_buildings : 1,
					suburb_max_buildings != null ? suburb_max_buildings : 2,
					center_min_buildings != null ? center_min_buildings : 3,
					center_max_buildings != null ? center_max_buildings : 5,
					suburb_height_mult != null ? suburb_height_mult : 0.6f,
					center_height_mult != null ? center_height_mult : 1.2f,
					noise_scale != null ? noise_scale : 256,
					toxic_garden_chance != null ? toxic_garden_chance : 0.05f,
					road_buffer != null ? road_buffer : 2,
					garden_tree_chance != null ? garden_tree_chance : 0.5f,
					trees);
		}
	}

	public record PlanetBiomeCityVaultJsonInfo(Boolean enabled, Float chance, List<String> blocks,
			List<String> accent_blocks) implements JsonInfo<PlanetBiomeCityVault> {
		public PlanetBiomeCityVault cast() {
			List<BlockState> vaultBlocks = blocks == null ? List.of()
					: blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			List<BlockState> accentBlocks = accent_blocks == null ? List.of()
					: accent_blocks.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
			return new PlanetBiomeCityVault(
					enabled != null ? enabled : false,
					chance != null ? chance : 0.1f,
					vaultBlocks,
					accentBlocks);
		}
	}

	public record PlanetBiomeCityGridJsonInfo(boolean enabled, int road_width, int block_size,
			List<String> road_blocks, List<String> gradient_blocks, List<String> accent_blocks,
			float accent_chance, int road_smooth, int slope_check, int headroom,
			int bridge_threshold, int tunnel_threshold, int max_height_diff,
			List<PlanetBiomeRuinJsonInfo> ruins, PlanetBiomeCityOvergrownJsonInfo overgrown,
			PlanetBiomeCityUrbanJsonInfo urban, PlanetBiomeCityVaultJsonInfo vault) implements JsonInfo<PlanetBiomeCityGrid> {
		public PlanetBiomeCityGrid cast() {
			List<BlockState> roadBlocks = road_blocks().stream().map(PlanetBiomeJsonInfo::getBlock)
					.collect(Collectors.toList());
			List<BlockState> gradientBlocks = gradient_blocks().stream().map(PlanetBiomeJsonInfo::getBlock)
					.collect(Collectors.toList());
			List<BlockState> accentBlocks = accent_blocks().stream().map(PlanetBiomeJsonInfo::getBlock)
					.collect(Collectors.toList());
			List<PlanetBiomeRuin> ruinsList = ruins().stream().map(PlanetBiomeRuinJsonInfo::cast)
					.collect(Collectors.toList());
			PlanetBiomeCityOvergrown overgrownConfig = overgrown == null ? PlanetBiomeCityOvergrown.DISABLED : overgrown.cast();
			PlanetBiomeCityUrban urbanConfig = urban == null ? PlanetBiomeCityUrban.DEFAULT : urban.cast();
			PlanetBiomeCityVault vaultConfig = vault == null ? PlanetBiomeCityVault.DISABLED : vault.cast();
			return new PlanetBiomeCityGrid(enabled, road_width, block_size, roadBlocks,
					gradientBlocks, accentBlocks, accent_chance, road_smooth, slope_check,
					headroom, bridge_threshold, tunnel_threshold, max_height_diff, ruinsList,
					overgrownConfig, urbanConfig, vaultConfig);
		}
	}

	public record PlanetBiomeClientSettingsJsonInfo(int weather_tint) implements JsonInfo<PlanetBiomeClientSettings> {
		public PlanetBiomeClientSettings cast() {
			return new PlanetBiomeClientSettings(weather_tint);
		}
	}

	@Override
	public PlanetBiomeSettings cast() {
		List<BlockState> tops = top.stream().map(PlanetBiomeJsonInfo::getBlock).collect(Collectors.toList());
		PlanetBiomeBlocks blocks = new PlanetBiomeBlocks(getBlock(base), tops, getBlock(second), getBlock(stair),
				getBlock(slab), getBlock(extra));
		List<PlanetBiomeTree> trees = trees().stream().map(PlanetBiomeTreeJsonInfo::cast).collect(Collectors.toList());
		List<PlanetBiomeBush> bushes = bushes().stream().map(PlanetBiomeBushJsonInfo::cast)
				.collect(Collectors.toList());
		PlanetBiomeGrass grass = grass().cast();
		PlanetBiomeLakes lakes = lakes().cast();
		List<PlanetBiomeRock> rocks = rocks().stream().map(PlanetBiomeRockJsonInfo::cast).collect(Collectors.toList());
		List<PlanetBiomeBigRock> big_rocks = big_rocks().stream().map(PlanetBiomeBigRockJsonInfo::cast)
				.collect(Collectors.toList());
		PlanetBiomeCityGrid cityGrid = city_grid() == null ? PlanetBiomeCityGrid.DISABLED : city_grid().cast();
		PlanetBiomeClientSettings settings = client_settings() == null ? PlanetBiomeClientSettings.DEFAULT
				: client_settings().cast();
		return new PlanetBiomeSettings(ResourceLocation.parse(home), blocks, ResourceLocation.parse(surface), trees,
				bushes, grass, lakes, rocks, big_rocks, cityGrid, settings);
	}
}