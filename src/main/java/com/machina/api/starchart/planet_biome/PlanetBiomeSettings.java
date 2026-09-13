package com.machina.api.starchart.planet_biome;

import java.util.List;

import com.machina.api.starchart.planet_biome.placement.PlacementModifier;
import com.machina.api.util.block.WeightedStateProviderProvider;
import com.machina.api.util.reflect.MachinaCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetBiomeSettings(ResourceLocation home, PlanetBiomeBlocks blocks, ResourceLocation surface,
		List<PlanetBiomeTree> trees, List<PlanetBiomeBush> bushes, PlanetBiomeGrass grass, PlanetBiomeLakes lakes,
		List<PlanetBiomeRock> rocks, List<PlanetBiomeBigRock> big_rocks,
		PlanetBiomeCityGrid city_grid, PlanetBiomeClientSettings client_settings) {

	public static enum PlanetHightechRuinShape {
		TUBE, PILLAR, RUBBLE, FOUNDATION, WALL
	}

	public BlockState base() {
		return blocks.base();
	}

	public List<BlockState> top() {
		return blocks.top();
	}

	public BlockState second() {
		return blocks.second();
	}

	public BlockState stair() {
		return blocks.stair();
	}

	public BlockState slab() {
		return blocks.slab();
	}

	public BlockState extra() {
		return blocks.extra();
	}

	private static WeightedStateProviderProvider.Builder weighted(List<PlanetBlockWeight> gs) {
		WeightedStateProviderProvider.Builder builder = WeightedStateProviderProvider.builder();
		for (PlanetBlockWeight g : gs) {
			builder.add(g.block(), g.weight());
		}
		return builder;
	}

	public static final Codec<PlanetBiomeSettings> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ResourceLocation.CODEC.fieldOf("home").forGetter(PlanetBiomeSettings::home),
				PlanetBiomeBlocks.CODEC_FLAT.forGetter(PlanetBiomeSettings::blocks),
				ResourceLocation.CODEC.fieldOf("surface").forGetter(PlanetBiomeSettings::surface),
				Codec.list(PlanetBiomeTree.CODEC).fieldOf("trees").forGetter(PlanetBiomeSettings::trees),
				Codec.list(PlanetBiomeBush.CODEC).fieldOf("bushes").forGetter(PlanetBiomeSettings::bushes),
				PlanetBiomeGrass.CODEC.fieldOf("grass").forGetter(PlanetBiomeSettings::grass),
				PlanetBiomeLakes.CODEC.fieldOf("lakes").forGetter(PlanetBiomeSettings::lakes),
				Codec.list(PlanetBiomeRock.CODEC).fieldOf("rocks").forGetter(PlanetBiomeSettings::rocks),
				Codec.list(PlanetBiomeBigRock.CODEC).fieldOf("big_rocks").forGetter(PlanetBiomeSettings::big_rocks),
				PlanetBiomeCityGrid.CODEC.optionalFieldOf("city_grid", PlanetBiomeCityGrid.DISABLED).forGetter(PlanetBiomeSettings::city_grid),
				PlanetBiomeClientSettings.CODEC.fieldOf("client_settings")
						.forGetter(PlanetBiomeSettings::client_settings))
		.apply(instance, PlanetBiomeSettings::new);
	});

	public record PlanetBiomeBlocks(BlockState base, List<BlockState> top, BlockState second, BlockState stair,
			BlockState slab, BlockState extra) {
		public static final Codec<PlanetBiomeBlocks> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeBlocks::base),
						Codec.list(BlockState.CODEC).fieldOf("top").forGetter(PlanetBiomeBlocks::top),
						BlockState.CODEC.fieldOf("second").forGetter(PlanetBiomeBlocks::second),
						BlockState.CODEC.fieldOf("stair").forGetter(PlanetBiomeBlocks::stair),
						BlockState.CODEC.fieldOf("slab").forGetter(PlanetBiomeBlocks::slab),
						BlockState.CODEC.fieldOf("extra").forGetter(PlanetBiomeBlocks::extra))
				.apply(instance, PlanetBiomeBlocks::new));

		// Flat codec that reads from top-level JSON fields instead of nested
		public static final com.mojang.serialization.MapCodec<PlanetBiomeBlocks> CODEC_FLAT = RecordCodecBuilder.mapCodec(instance -> instance
				.group(BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeBlocks::base),
						Codec.list(BlockState.CODEC).fieldOf("top").forGetter(PlanetBiomeBlocks::top),
						BlockState.CODEC.fieldOf("second").forGetter(PlanetBiomeBlocks::second),
						BlockState.CODEC.fieldOf("stair").forGetter(PlanetBiomeBlocks::stair),
						BlockState.CODEC.fieldOf("slab").forGetter(PlanetBiomeBlocks::slab),
						BlockState.CODEC.fieldOf("extra").forGetter(PlanetBiomeBlocks::extra))
				.apply(instance, PlanetBiomeBlocks::new));
	}

	public record PlanetBiomeEffects(int fog_color, int sky_color, int water_color, int water_fog_color,
			int grass_color) {

		public static final Codec<PlanetBiomeEffects> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("fog_color").forGetter(PlanetBiomeEffects::fog_color),
						Codec.INT.fieldOf("sky_color").forGetter(PlanetBiomeEffects::sky_color),
						Codec.INT.fieldOf("water_color").forGetter(PlanetBiomeEffects::water_color),
						Codec.INT.fieldOf("water_fog_color").forGetter(PlanetBiomeEffects::water_fog_color),
						Codec.INT.fieldOf("grass_color").forGetter(PlanetBiomeEffects::grass_color))
				.apply(instance, PlanetBiomeEffects::new));

		public BiomeSpecialEffects.Builder build() {
			return new BiomeSpecialEffects.Builder().fogColor(fog_color).skyColor(sky_color).waterColor(water_color)
					.waterFogColor(water_fog_color).grassColorOverride(grass_color);
		}
	}

	public record PlanetBiomeTree(ResourceLocation tree, List<BlockState> blocks, float chance, List<BlockState> fruit,
			List<Direction> fruit_dirs, float fruit_chance, float tree_fruit_chance) {

		public static final Codec<PlanetBiomeTree> CODEC = RecordCodecBuilder
				.create(instance -> instance
						.group(ResourceLocation.CODEC.fieldOf("tree").forGetter(PlanetBiomeTree::tree),
								Codec.list(BlockState.CODEC).fieldOf("blocks").forGetter(PlanetBiomeTree::blocks),
								Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeTree::chance),
								Codec.list(BlockState.CODEC).fieldOf("fruit").forGetter(PlanetBiomeTree::fruit),
								Codec.list(Direction.CODEC).fieldOf("fruit_dirs")
										.forGetter(PlanetBiomeTree::fruit_dirs),
								Codec.FLOAT.fieldOf("fruit_chance").forGetter(PlanetBiomeTree::fruit_chance),
								Codec.FLOAT.fieldOf("tree_fruit_chance").forGetter(PlanetBiomeTree::tree_fruit_chance))
						.apply(instance, PlanetBiomeTree::new));

		public BlockState wood() {
			return blocks.get(0);
		}

		public BlockState leaves() {
			return blocks.get(1);
		}

		public BlockState stem() {
			return blocks.get(0);
		}

		public BlockState cap() {
			return blocks.get(1);
		}

		public BlockState gills() {
			return blocks.get(2);
		}
	}

	public record PlanetBiomeBush(BlockState block, float radius, float chance) {

		public static final Codec<PlanetBiomeBush> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("bush").forGetter(PlanetBiomeBush::block),
						Codec.FLOAT.fieldOf("radius").forGetter(PlanetBiomeBush::radius),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeBush::chance))
				.apply(instance, PlanetBiomeBush::new));
	}

	public record PlanetBiomeGrass(boolean enabled, int min, int max, WeightedStateProviderProvider provider) {

		public PlanetBiomeGrass(boolean enabled, int min, int max, List<PlanetBlockWeight> grasses) {
			this(enabled, min, max, weighted(grasses).build());
		}

		public static final Codec<PlanetBiomeGrass> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.BOOL.fieldOf("enabled").forGetter(PlanetBiomeGrass::enabled),
						Codec.INT.fieldOf("min").forGetter(PlanetBiomeGrass::min),
						Codec.INT.fieldOf("max").forGetter(PlanetBiomeGrass::max),
						WeightedStateProviderProvider.CODEC.fieldOf("provider").forGetter(PlanetBiomeGrass::provider))
				.apply(instance, PlanetBiomeGrass::new));
	}

	public record PlanetBiomeLakes(BlockState base, boolean enabled, float chance, float decorator_chance,
			WeightedStateProviderProvider provider) {

		public PlanetBiomeLakes(BlockState base, boolean enabled, float chance, float decorator_chance,
				List<PlanetBlockWeight> decorators) {
			this(base, enabled, chance, decorator_chance, weighted(decorators).build());
		}

		public static final Codec<PlanetBiomeLakes> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeLakes::base),
						Codec.BOOL.fieldOf("enabled").forGetter(PlanetBiomeLakes::enabled),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeLakes::chance),
						Codec.FLOAT.fieldOf("decorator_chance").forGetter(PlanetBiomeLakes::decorator_chance),
						WeightedStateProviderProvider.CODEC.fieldOf("provider").forGetter(PlanetBiomeLakes::provider))
				.apply(instance, PlanetBiomeLakes::new));
	}

	public record PlanetBiomeRock(BlockState base, BlockState stair, BlockState slab, BlockState wall, float chance,
			float radius, float deform, List<PlacementModifier> modifiers) {

		public static final Codec<PlanetBiomeRock> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeRock::base),
						BlockState.CODEC.fieldOf("stair").forGetter(PlanetBiomeRock::stair),
						BlockState.CODEC.fieldOf("slab").forGetter(PlanetBiomeRock::slab),
						BlockState.CODEC.fieldOf("wall").forGetter(PlanetBiomeRock::wall),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeRock::chance),
						Codec.FLOAT.fieldOf("radius").forGetter(PlanetBiomeRock::radius),
						Codec.FLOAT.fieldOf("deform").forGetter(PlanetBiomeRock::deform), MachinaCodecs.MODIFIER_CODEC
								.listOf().optionalFieldOf("modifiers", List.of()).forGetter(PlanetBiomeRock::modifiers))
				.apply(instance, PlanetBiomeRock::new));
	}

	public record PlanetBiomeBigRock(ResourceLocation rock, BlockState block, BlockState extra, float chance,
			float up_extra_chance, float down_extra_chance, float side_extra_chance,
			List<PlacementModifier> modifiers) {

		public static final Codec<PlanetBiomeBigRock> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(ResourceLocation.CODEC.fieldOf("rock").forGetter(PlanetBiomeBigRock::rock),
						BlockState.CODEC.fieldOf("block").forGetter(PlanetBiomeBigRock::block),
						BlockState.CODEC.fieldOf("extra").forGetter(PlanetBiomeBigRock::extra),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeBigRock::chance),
						Codec.FLOAT.fieldOf("up_extra_chance").forGetter(PlanetBiomeBigRock::up_extra_chance),
						Codec.FLOAT.fieldOf("down_extra_chance").forGetter(PlanetBiomeBigRock::down_extra_chance),
						Codec.FLOAT.fieldOf("side_extra_chance").forGetter(PlanetBiomeBigRock::side_extra_chance),
						MachinaCodecs.MODIFIER_CODEC.listOf().optionalFieldOf("modifiers", List.of())
								.forGetter(PlanetBiomeBigRock::modifiers))
						.apply(instance, PlanetBiomeBigRock::new));
	}

	public record PlanetBlockWeight(BlockState block, int weight) {
		public static final Codec<PlanetBlockWeight> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("block").forGetter(PlanetBlockWeight::block),
						Codec.INT.fieldOf("weight").forGetter(PlanetBlockWeight::weight))
				.apply(instance, PlanetBlockWeight::new));
	}

	public record PlanetBiomeRuin(PlanetHightechRuinShape shape, int min_size, int max_size, int min_height,
			int max_height, float chance, float min_weathering, float max_weathering) {
		public static final Codec<PlanetBiomeRuin> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(MachinaCodecs.enumCodec(PlanetHightechRuinShape.class).fieldOf("shape").forGetter(
						PlanetBiomeRuin::shape), Codec.INT.fieldOf("min_size").forGetter(PlanetBiomeRuin::min_size),
						Codec.INT.fieldOf("max_size").forGetter(PlanetBiomeRuin::max_size),
						Codec.INT.fieldOf("min_height").forGetter(PlanetBiomeRuin::min_height),
						Codec.INT.fieldOf("max_height").forGetter(PlanetBiomeRuin::max_height),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeRuin::chance),
						Codec.FLOAT.fieldOf("min_weathering").forGetter(PlanetBiomeRuin::min_weathering),
						Codec.FLOAT.fieldOf("max_weathering").forGetter(PlanetBiomeRuin::max_weathering))
				.apply(instance, PlanetBiomeRuin::new));
	}

	public record PlanetBiomeCityGrid(boolean enabled, int road_width, int block_size,
			List<BlockState> road_blocks, List<BlockState> gradient_blocks, List<BlockState> accent_blocks,
			float accent_chance, int road_smooth, int slope_check, int headroom,
			int bridge_threshold, int tunnel_threshold, int max_height_diff,
			List<PlanetBiomeRuin> ruins) {
		public static final PlanetBiomeCityGrid DISABLED = new PlanetBiomeCityGrid(false, 3, 16,
				java.util.Collections.emptyList(), java.util.Collections.emptyList(),
				java.util.Collections.emptyList(), 0.001f, 8, 4, 4, 2, 2, 8,
				java.util.Collections.emptyList());

		public static final Codec<PlanetBiomeCityGrid> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.BOOL.fieldOf("enabled").forGetter(PlanetBiomeCityGrid::enabled),
						Codec.INT.fieldOf("road_width").forGetter(PlanetBiomeCityGrid::road_width),
						Codec.INT.fieldOf("block_size").forGetter(PlanetBiomeCityGrid::block_size),
						Codec.list(BlockState.CODEC).fieldOf("road_blocks").forGetter(PlanetBiomeCityGrid::road_blocks),
						Codec.list(BlockState.CODEC).fieldOf("gradient_blocks").forGetter(PlanetBiomeCityGrid::gradient_blocks),
						Codec.list(BlockState.CODEC).fieldOf("accent_blocks").forGetter(PlanetBiomeCityGrid::accent_blocks),
						Codec.FLOAT.fieldOf("accent_chance").forGetter(PlanetBiomeCityGrid::accent_chance),
						Codec.INT.fieldOf("road_smooth").forGetter(PlanetBiomeCityGrid::road_smooth),
						Codec.INT.fieldOf("slope_check").forGetter(PlanetBiomeCityGrid::slope_check),
						Codec.INT.fieldOf("headroom").forGetter(PlanetBiomeCityGrid::headroom),
						Codec.INT.fieldOf("bridge_threshold").forGetter(PlanetBiomeCityGrid::bridge_threshold),
						Codec.INT.fieldOf("tunnel_threshold").forGetter(PlanetBiomeCityGrid::tunnel_threshold),
						Codec.INT.fieldOf("max_height_diff").forGetter(PlanetBiomeCityGrid::max_height_diff),
						Codec.list(PlanetBiomeRuin.CODEC).fieldOf("ruins").forGetter(PlanetBiomeCityGrid::ruins))
				.apply(instance, PlanetBiomeCityGrid::new));
	}

	public record PlanetBiomeClientSettings(int weather_tint) {
		public static final PlanetBiomeClientSettings DEFAULT = new PlanetBiomeClientSettings(0xFF_FFFFFF);

		public static final Codec<PlanetBiomeClientSettings> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.fieldOf("weather_tint").forGetter(PlanetBiomeClientSettings::weather_tint))
				.apply(instance, PlanetBiomeClientSettings::new));

		public static final StreamCodec<ByteBuf, PlanetBiomeClientSettings> STREAM_CODEC = StreamCodec
				.composite(ByteBufCodecs.INT, PlanetBiomeClientSettings::weather_tint, PlanetBiomeClientSettings::new);
	}
}
