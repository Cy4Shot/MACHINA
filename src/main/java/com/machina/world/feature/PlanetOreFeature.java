package com.machina.world.feature;

import java.util.BitSet;
import java.util.function.Function;

import com.machina.api.starchart.planet_type.PlanetType.PlanetOre;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PlanetOreFeature extends Feature<PlanetOreFeature.PlanetOreFeatureConfig> {

	public record PlanetOreFeatureConfig(PlanetOre ore, BlockState oreState) implements FeatureConfiguration {
		public static final Codec<PlanetOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(PlanetOre.CODEC.fieldOf("ore").forGetter(PlanetOreFeatureConfig::ore),
						BlockState.CODEC.fieldOf("oreState").forGetter(PlanetOreFeatureConfig::oreState))
				.apply(instance, PlanetOreFeatureConfig::new));
	}

	public PlanetOreFeature() {
		super(PlanetOreFeatureConfig.CODEC);
	}

	public boolean place(FeaturePlaceContext<PlanetOreFeature.PlanetOreFeatureConfig> ctx) {
		RandomSource randomsource = ctx.random();
		BlockPos blockpos = ctx.origin();
		WorldGenLevel worldgenlevel = ctx.level();
		PlanetOre cfg = ctx.config().ore();
		BlockState ore = ctx.config().oreState();

		// Prevent surface generation
		if (worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockpos.getX(), blockpos.getZ()) - 2 <= blockpos
				.getY())
			return false;

		float f = randomsource.nextFloat() * (float) Math.PI;
		float f1 = (float) cfg.size() / 8.0F;
		int i = Mth.ceil(((float) cfg.size() / 16.0F * 2.0F + 1.0F) / 2.0F);
		double d0 = (double) blockpos.getX() + Math.sin(f) * (double) f1;
		double d1 = (double) blockpos.getX() - Math.sin(f) * (double) f1;
		double d2 = (double) blockpos.getZ() + Math.cos(f) * (double) f1;
		double d3 = (double) blockpos.getZ() - Math.cos(f) * (double) f1;
		double d4 = blockpos.getY() + randomsource.nextInt(3) - 2;
		double d5 = blockpos.getY() + randomsource.nextInt(3) - 2;
		int k = blockpos.getX() - Mth.ceil(f1) - i;
		int l = blockpos.getY() - 2 - i;
		int i1 = blockpos.getZ() - Mth.ceil(f1) - i;
		int j1 = 2 * (Mth.ceil(f1) + i);
		int k1 = 2 * (2 + i);

		for (int l1 = k; l1 <= k + j1; ++l1) {
			for (int i2 = i1; i2 <= i1 + j1; ++i2) {
				if (l <= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2)) {
					return this.doPlace(worldgenlevel, randomsource, cfg, ore, d0, d1, d2, d3, d4, d5, k, l, i1, j1,
							k1);
				}
			}
		}

		return false;
	}

	protected boolean doPlace(WorldGenLevel level, RandomSource rand, PlanetOre cfg, BlockState ore, double p_225175_,
			double p_225176_, double p_225177_, double p_225178_, double p_225179_, double p_225180_, int p_225181_,
			int p_225182_, int p_225183_, int p_225184_, int p_225185_) {
		int i = 0;
		BitSet bitset = new BitSet(p_225184_ * p_225185_ * p_225184_);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		int j = cfg.size();
		double[] adouble = new double[j * 4];

		for (int k = 0; k < j; ++k) {
			float f = (float) k / (float) j;
			double d0 = Mth.lerp(f, p_225175_, p_225176_);
			double d1 = Mth.lerp(f, p_225179_, p_225180_);
			double d2 = Mth.lerp(f, p_225177_, p_225178_);
			double d3 = rand.nextDouble() * (double) j / 16.0D;
			double d4 = ((double) (Mth.sin((float) Math.PI * f) + 1.0F) * d3 + 1.0D) / 2.0D;
			adouble[k * 4] = d0;
			adouble[k * 4 + 1] = d1;
			adouble[k * 4 + 2] = d2;
			adouble[k * 4 + 3] = d4;
		}

		for (int l3 = 0; l3 < j - 1; ++l3) {
			if (!(adouble[l3 * 4 + 3] <= 0.0D)) {
				for (int i4 = l3 + 1; i4 < j; ++i4) {
					if (!(adouble[i4 * 4 + 3] <= 0.0D)) {
						double d8 = adouble[l3 * 4] - adouble[i4 * 4];
						double d10 = adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1];
						double d12 = adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2];
						double d14 = adouble[l3 * 4 + 3] - adouble[i4 * 4 + 3];
						if (d14 * d14 > d8 * d8 + d10 * d10 + d12 * d12) {
							if (d14 > 0.0D) {
								adouble[i4 * 4 + 3] = -1.0D;
							} else {
								adouble[l3 * 4 + 3] = -1.0D;
							}
						}
					}
				}
			}
		}

		try (BulkSectionAccess bulksectionaccess = new BulkSectionAccess(level)) {
			for (int j4 = 0; j4 < j; ++j4) {
				double d9 = adouble[j4 * 4 + 3];
				if (!(d9 < 0.0D)) {
					double d11 = adouble[j4 * 4];
					double d13 = adouble[j4 * 4 + 1];
					double d15 = adouble[j4 * 4 + 2];
					int k4 = Math.max(Mth.floor(d11 - d9), p_225181_);
					int l = Math.max(Mth.floor(d13 - d9), p_225182_);
					int i1 = Math.max(Mth.floor(d15 - d9), p_225183_);
					int j1 = Math.max(Mth.floor(d11 + d9), k4);
					int k1 = Math.max(Mth.floor(d13 + d9), l);
					int l1 = Math.max(Mth.floor(d15 + d9), i1);

					for (int i2 = k4; i2 <= j1; ++i2) {
						double d5 = ((double) i2 + 0.5D - d11) / d9;
						if (d5 * d5 < 1.0D) {
							for (int j2 = l; j2 <= k1; ++j2) {
								double d6 = ((double) j2 + 0.5D - d13) / d9;
								if (d5 * d5 + d6 * d6 < 1.0D) {
									for (int k2 = i1; k2 <= l1; ++k2) {
										double d7 = ((double) k2 + 0.5D - d15) / d9;
										if (d5 * d5 + d6 * d6 + d7 * d7 < 1.0D && !level.isOutsideBuildHeight(j2)) {
											int l2 = i2 - p_225181_ + (j2 - p_225182_) * p_225184_
													+ (k2 - p_225183_) * p_225184_ * p_225185_;
											if (!bitset.get(l2)) {
												bitset.set(l2);
												blockpos$mutableblockpos.set(i2, j2, k2);
												if (level.ensureCanWrite(blockpos$mutableblockpos)) {
													LevelChunkSection levelchunksection = bulksectionaccess
															.getSection(blockpos$mutableblockpos);
													if (levelchunksection != null) {
														int i3 = SectionPos.sectionRelative(i2);
														int j3 = SectionPos.sectionRelative(j2);
														int k3 = SectionPos.sectionRelative(k2);
														BlockState blockstate = levelchunksection.getBlockState(i3, j3,
																k3);

														if (canPlaceOre(blockstate, bulksectionaccess::getBlockState,
																rand, cfg, blockpos$mutableblockpos)) {
															levelchunksection.setBlockState(i3, j3, k3, ore, false);
															++i;
															break;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return i > 0;
	}

	public static boolean canPlaceOre(BlockState state, Function<BlockPos, BlockState> p_225188_,
			RandomSource p_225189_, PlanetOre cfg, BlockPos.MutableBlockPos p_225192_) {
		if (!state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
			return false;
		} else if (shouldSkipAirCheck(p_225189_, cfg.exposure_removal_chance())) {
			return true;
		} else {
			return !isAdjacentToAir(p_225188_, p_225192_);
		}
	}

	protected static boolean shouldSkipAirCheck(RandomSource p_225169_, float p_225170_) {
		if (p_225170_ <= 0.0F) {
			return true;
		} else if (p_225170_ >= 1.0F) {
			return false;
		} else {
			return p_225169_.nextFloat() >= p_225170_;
		}
	}
}
