package com.machina.api.starchart.planet_biome;

import java.util.List;

import com.machina.api.util.block.WeightedStateProviderProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.state.BlockState;

public record PlanetBiomeSettings(PlanetBiomeEffects effects, BlockState base, ResourceLocation surface,
		List<BlockState> top, BlockState second, BlockState stair, BlockState slab, BlockState extra,
		List<PlanetBiomeTree> trees, List<PlanetBiomeBush> bushes, PlanetBiomeGrass grass, PlanetBiomeLakes lakes,
		List<PlanetBiomeRock> rocks, List<PlanetBiomeBigRock> big_rocks, List<PlanetBiomeOre> ores) {

	private static WeightedStateProviderProvider.Builder weighted(List<PlanetBlockWeight> gs) {
		WeightedStateProviderProvider.Builder builder = WeightedStateProviderProvider.builder();
		for (PlanetBlockWeight g : gs) {
			builder.add(g.block(), g.weight());
		}
		return builder;
	}

	public static final Codec<PlanetBiomeSettings> CODEC = RecordCodecBuilder
			.create(instance -> instance
					.group(PlanetBiomeEffects.CODEC.fieldOf("effects").forGetter(PlanetBiomeSettings::effects),
							BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeSettings::base),
							ResourceLocation.CODEC.fieldOf("surface").forGetter(PlanetBiomeSettings::surface),
							Codec.list(BlockState.CODEC).fieldOf("top").forGetter(PlanetBiomeSettings::top),
							BlockState.CODEC.fieldOf("second").forGetter(PlanetBiomeSettings::second),
							BlockState.CODEC.fieldOf("stair").forGetter(PlanetBiomeSettings::stair),
							BlockState.CODEC.fieldOf("slab").forGetter(PlanetBiomeSettings::slab),
							BlockState.CODEC.fieldOf("extra").forGetter(PlanetBiomeSettings::extra),
							Codec.list(PlanetBiomeTree.CODEC).fieldOf("trees").forGetter(PlanetBiomeSettings::trees),
							Codec.list(PlanetBiomeBush.CODEC).fieldOf("bushes").forGetter(PlanetBiomeSettings::bushes),
							PlanetBiomeGrass.CODEC.fieldOf("grass").forGetter(PlanetBiomeSettings::grass),
							PlanetBiomeLakes.CODEC.fieldOf("lakes").forGetter(PlanetBiomeSettings::lakes),
							Codec.list(PlanetBiomeRock.CODEC).fieldOf("rocks").forGetter(PlanetBiomeSettings::rocks),
							Codec.list(PlanetBiomeBigRock.CODEC).fieldOf("big_rocks")
									.forGetter(PlanetBiomeSettings::big_rocks),
							Codec.list(PlanetBiomeOre.CODEC).fieldOf("ores").forGetter(PlanetBiomeSettings::ores))
					.apply(instance, PlanetBiomeSettings::new));

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
			float radius, float deform) {

		public static final Codec<PlanetBiomeRock> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("base").forGetter(PlanetBiomeRock::base),
						BlockState.CODEC.fieldOf("stair").forGetter(PlanetBiomeRock::stair),
						BlockState.CODEC.fieldOf("slab").forGetter(PlanetBiomeRock::slab),
						BlockState.CODEC.fieldOf("wall").forGetter(PlanetBiomeRock::wall),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeRock::chance),
						Codec.FLOAT.fieldOf("radius").forGetter(PlanetBiomeRock::radius),
						Codec.FLOAT.fieldOf("deform").forGetter(PlanetBiomeRock::deform))
				.apply(instance, PlanetBiomeRock::new));

	}

	public record PlanetBiomeBigRock(ResourceLocation rock, BlockState block, BlockState extra, float chance,
			float up_extra_chance, float down_extra_chance, float side_extra_chance) {

		public static final Codec<PlanetBiomeBigRock> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(ResourceLocation.CODEC.fieldOf("rock").forGetter(PlanetBiomeBigRock::rock),
						BlockState.CODEC.fieldOf("block").forGetter(PlanetBiomeBigRock::block),
						BlockState.CODEC.fieldOf("extra").forGetter(PlanetBiomeBigRock::extra),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeBigRock::chance),
						Codec.FLOAT.fieldOf("up_extra_chance").forGetter(PlanetBiomeBigRock::up_extra_chance),
						Codec.FLOAT.fieldOf("down_extra_chance").forGetter(PlanetBiomeBigRock::down_extra_chance),
						Codec.FLOAT.fieldOf("side_extra_chance").forGetter(PlanetBiomeBigRock::side_extra_chance))
				.apply(instance, PlanetBiomeBigRock::new));
	}

	public record PlanetBiomeOre(BlockState block, int size, float exposure_removal_chance, float chance, int min_y,
			int max_y) {

		public static final Codec<PlanetBiomeOre> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("block").forGetter(PlanetBiomeOre::block),
						Codec.INT.fieldOf("size").forGetter(PlanetBiomeOre::size),
						Codec.FLOAT.fieldOf("exposure_removal_chance")
								.forGetter(PlanetBiomeOre::exposure_removal_chance),
						Codec.FLOAT.fieldOf("chance").forGetter(PlanetBiomeOre::chance),
						Codec.INT.fieldOf("min_y").forGetter(PlanetBiomeOre::min_y),
						Codec.INT.fieldOf("max_y").forGetter(PlanetBiomeOre::max_y))
				.apply(instance, PlanetBiomeOre::new));
	}

	public record PlanetBlockWeight(BlockState block, int weight) {
		public static final Codec<PlanetBlockWeight> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("block").forGetter(PlanetBlockWeight::block),
						Codec.INT.fieldOf("weight").forGetter(PlanetBlockWeight::weight))
				.apply(instance, PlanetBlockWeight::new));
	}

}
