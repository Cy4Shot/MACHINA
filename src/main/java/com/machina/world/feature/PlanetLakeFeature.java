package com.machina.world.feature;

import com.machina.api.starchart.obj.Planet;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.block.WeightedStateProviderProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class PlanetLakeFeature extends Feature<PlanetLakeFeature.PlanetLakeFeatureConfig> {

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public record PlanetLakeFeatureConfig(BlockState state, float decorator_chance,
			WeightedStateProviderProvider provider) implements FeatureConfiguration {
		public static final Codec<PlanetLakeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(BlockState.CODEC.fieldOf("state").forGetter(PlanetLakeFeatureConfig::state),
						Codec.FLOAT.fieldOf("decorator_chance").forGetter(PlanetLakeFeatureConfig::decorator_chance),
						WeightedStateProviderProvider.CODEC.fieldOf("provider")
								.forGetter(PlanetLakeFeatureConfig::provider))
				.apply(instance, PlanetLakeFeatureConfig::new));
	}

	public PlanetLakeFeature() {
		super(PlanetLakeFeatureConfig.CODEC);
	}

	public boolean place(FeaturePlaceContext<PlanetLakeFeature.PlanetLakeFeatureConfig> ctx) {
		BlockPos pos = ctx.origin();
		WorldGenLevel level = ctx.level();
		RandomSource rand = ctx.random();
		PlanetLakeFeature.PlanetLakeFeatureConfig cfg = ctx.config();

		Planet p = PlanetHelper.getPlanetFor(level.getLevel());
        BlockState fluid = null;
        if (p != null) {
            fluid = p.getDominantLiquidBodyBlock();
        }
        if (fluid == null || fluid.getFluidState().isEmpty()) {
			fluid = Blocks.WATER.defaultBlockState();
		}

		if (pos.getY() <= level.getMinBuildHeight() + 4) {
			return false;
		} else {
			pos = pos.below(4);
			boolean[] aboolean = new boolean[2048];
			int i = rand.nextInt(4) + 4;

			for (int j = 0; j < i; ++j) {
				double d0 = rand.nextDouble() * 6.0D + 3.0D;
				double d1 = rand.nextDouble() * 4.0D + 2.0D;
				double d2 = rand.nextDouble() * 6.0D + 3.0D;
				double d3 = rand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
				double d4 = rand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
				double d5 = rand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

				for (int l = 1; l < 15; ++l) {
					for (int i1 = 1; i1 < 15; ++i1) {
						for (int j1 = 1; j1 < 7; ++j1) {
							double d6 = ((double) l - d3) / (d0 / 2.0D);
							double d7 = ((double) j1 - d4) / (d1 / 2.0D);
							double d8 = ((double) i1 - d5) / (d2 / 2.0D);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;
							if (d9 < 1.0D) {
								aboolean[(l * 16 + i1) * 8 + j1] = true;
							}
						}
					}
				}
			}

			for (int k1 = 0; k1 < 16; ++k1) {
				for (int k = 0; k < 16; ++k) {
					for (int l2 = 0; l2 < 8; ++l2) {
						boolean flag = !aboolean[(k1 * 16 + k) * 8 + l2]
								&& (k1 < 15 && aboolean[((k1 + 1) * 16 + k) * 8 + l2]
										|| k1 > 0 && aboolean[((k1 - 1) * 16 + k) * 8 + l2]
										|| k < 15 && aboolean[(k1 * 16 + k + 1) * 8 + l2]
										|| k > 0 && aboolean[(k1 * 16 + (k - 1)) * 8 + l2]
										|| l2 < 7 && aboolean[(k1 * 16 + k) * 8 + l2 + 1]
										|| l2 > 0 && aboolean[(k1 * 16 + k) * 8 + (l2 - 1)]);
						if (flag) {
							BlockState state3 = level.getBlockState(pos.offset(k1, l2, k));
							if (l2 >= 4 && state3.liquid())
								return false;
							if (l2 < 4 && !state3.isSolid() && level.getBlockState(pos.offset(k1, l2, k)) != fluid)
								return false;
						}
					}
				}
			}

			for (int l1 = 0; l1 < 16; ++l1) {
				for (int i2 = 0; i2 < 16; ++i2) {
					for (int i3 = 0; i3 < 8; ++i3) {
						if (aboolean[(l1 * 16 + i2) * 8 + i3]) {
							BlockPos pos1 = pos.offset(l1, i3, i2);
							if (this.canReplaceBlock(level.getBlockState(pos1))) {
								boolean flag1 = i3 >= 4;
								level.setBlock(pos1, flag1 ? AIR : fluid, 2);
								if (flag1) {
									level.scheduleTick(pos1, AIR.getBlock(), 0);
									this.markAboveForPostProcessing(level, pos1);
								}
							}
						}
					}
				}
			}

			BlockState state2 = cfg.state();
			if (!state2.isAir()) {
				for (int j2 = 0; j2 < 16; ++j2) {
					for (int j3 = 0; j3 < 16; ++j3) {
						for (int l3 = 0; l3 < 8; ++l3) {
							boolean flag2 = !aboolean[(j2 * 16 + j3) * 8 + l3]
									&& (j2 < 15 && aboolean[((j2 + 1) * 16 + j3) * 8 + l3]
											|| j2 > 0 && aboolean[((j2 - 1) * 16 + j3) * 8 + l3]
											|| j3 < 15 && aboolean[(j2 * 16 + j3 + 1) * 8 + l3]
											|| j3 > 0 && aboolean[(j2 * 16 + (j3 - 1)) * 8 + l3]
											|| l3 < 7 && aboolean[(j2 * 16 + j3) * 8 + l3 + 1]
											|| l3 > 0 && aboolean[(j2 * 16 + j3) * 8 + (l3 - 1)]);
							if (flag2 && (l3 < 4 || rand.nextInt(2) != 0)) {
								BlockPos n = pos.offset(j2, l3, j3);
								if (level.getBlockState(n).isSolid()) {
									BlockPos pos3 = pos.offset(j2, l3, j3);
									boolean flag3 = (l3 > 4 && level.getBlockState(n.above()).isSolid())
											|| level.getBlockState(n.below()).isSolid();
									level.setBlock(pos3, flag3 ? state2 : AIR, 2);
									this.markAboveForPostProcessing(level, pos3);
								} else {
									if (rand.nextFloat() < cfg.decorator_chance()) {
										BlockPos decpos = pos.offset(j2, 4, j3);
										BlockState deco = cfg.provider().getState(rand, decpos);
										if (deco.canSurvive(level, decpos)) {
											// Apply random horizontal facing if the blockstate supports it
											if (deco.getOptionalValue(BlockStateProperties.HORIZONTAL_FACING)
													.isPresent()) {
												deco = deco.setValue(BlockStateProperties.HORIZONTAL_FACING,
														Direction.Plane.HORIZONTAL.getRandomDirection(rand));
											}

											level.setBlock(decpos, deco, 2);
											level.getChunk(decpos).markPosForPostprocessing(decpos);
										}
									}
								}
							}
						}
					}
				}
			}

			return true;
		}
	}

	protected void markAboveForPostProcessing(@NotNull WorldGenLevel level, BlockPos pos) {
		BlockPos.MutableBlockPos posm = pos.mutable();

		for (int i = 0; i < 2; ++i) {
			posm.move(Direction.UP);
			if (level.getBlockState(posm).isAir()) {
				return;
			}

			level.getChunk(posm).markPosForPostprocessing(posm);
		}

	}

	private boolean canReplaceBlock(BlockState state) {
		return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
	}

}
