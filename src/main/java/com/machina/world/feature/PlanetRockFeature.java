package com.machina.world.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeRock;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.primitive.SDFSphere;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetRockFeature extends Feature<PlanetRockFeature.PlanetRockFeatureConfig> {

	public record PlanetRockFeatureConfig(PlanetBiomeRock rock) implements FeatureConfiguration {
		public static final Codec<PlanetRockFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(PlanetBiomeRock.CODEC.fieldOf("rock").forGetter(PlanetRockFeatureConfig::rock))
				.apply(instance, PlanetRockFeatureConfig::new));
	}

	private static final Direction[] EXTRA_DIRS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST, Direction.UP };
	private static final List<Direction> HORIZONTAL_DIRS = new ArrayList<>(
			Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));

	public PlanetRockFeature() {
		super(PlanetRockFeatureConfig.CODEC);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean place(FeaturePlaceContext<PlanetRockFeatureConfig> ctx) {
		PlanetBiomeRock config = ctx.config().rock();
		RandomSource rand = ctx.random();
		WorldGenLevel level = ctx.level();
		BlockPos pos = ctx.origin();

		int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
		BlockPos place = new BlockPos(pos.getX(), y, pos.getZ());

		SDF blob = new SDFSphere(1.01f).setBlock(config.base());
		blob = new SDFDisplacement(blob, rand, 2f);

		Set<BlockPos> positions = blob.fillRecursiveShift(level, place, p -> {
			if (!level.getBlockState(p.immutable()).isAir()) {
				return false;
			}
			int shifted = 0;
			while (!level.getBlockState(p.immutable().below()).isSolid()) {
				p.move(Direction.DOWN);
				if (++shifted > 3) {
					return false;
				}
			}
			return level.getBlockState(p.immutable()).isAir();
		});

		Set<BlockPos> two = new HashSet<>();
		for (BlockPos p : positions) {
			for (Direction d : EXTRA_DIRS) {
				BlockPos p2 = p.relative(d);
				if (!positions.contains(p2))
					two.add(p2);
			}
		}

		List<BlockPos> last = two.stream()
				.filter(p -> !level.getBlockState(p).isSolid() && level.getBlockState(p.below()).isSolid()).toList();

		for (BlockPos p : last) {
			if (rand.nextFloat() < 0.3f) {
				BlockState state = null;
				switch (rand.nextInt(3)) {
				case 0:
					MathUtil.shuffle(HORIZONTAL_DIRS, rand);
					for (Direction d : HORIZONTAL_DIRS) {
						if (level.getBlockState(p.relative(d)).is(config.base().getBlock())) {
							state = config.stair().setValue(StairBlock.FACING, d);
							break;
						}
					}
					if (state != null)
						break;
				case 1:
					state = config.slab();
					break;
				default:
					if (rand.nextFloat() < 0.5f) {
						MathUtil.shuffle(HORIZONTAL_DIRS, rand);
						for (Direction d : HORIZONTAL_DIRS) {
							if (level.getBlockState(p.relative(d)).is(config.base().getBlock())) {
								state = config.wall();
								break;
							}
						}
					}
				}
				if (state != null)
					level.setBlock(p, state, 3);
			}
		}

		return true;
	}

}
