package com.machina.api.world.builder;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class DynamicDimensionSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

	public static final DynamicDimensionSurfaceBuilder BASE = new DynamicDimensionSurfaceBuilder(
			SurfaceBuilderConfig.CODEC);

	public DynamicDimensionSurfaceBuilder(Codec<SurfaceBuilderConfig> p_i232124_1_) {
		super(p_i232124_1_);
	}

	public void apply(Random p_205610_1_, IChunk p_205610_2_, Biome p_205610_3_, int p_205610_4_, int p_205610_5_,
			int p_205610_6_, double p_205610_7_, BlockState p_205610_9_, BlockState p_205610_10_, int p_205610_11_,
			long p_205610_12_, SurfaceBuilderConfig p_205610_14_) {
		this.apply(p_205610_1_, p_205610_2_, p_205610_4_, p_205610_5_, p_205610_6_, p_205610_7_, p_205610_9_,
				p_205610_10_, p_205610_14_.getTopMaterial(), p_205610_14_.getUnderMaterial(),
				p_205610_14_.getUnderwaterMaterial(), p_205610_11_);
	}

	@SuppressWarnings("deprecation")
	protected void apply(Random p_206967_1_, IChunk p_206967_2_, int p_206967_4_, int p_206967_5_, int p_206967_6_,
			double p_206967_7_, BlockState p_206967_9_, BlockState p_206967_10_, BlockState p_206967_11_,
			BlockState p_206967_12_, BlockState p_206967_13_, int p_206967_14_) {
		BlockState blockstate = p_206967_11_;
		BlockState blockstate1 = p_206967_12_;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = -1;
		int j = (int) (p_206967_7_ / 3.0D + 3.0D + p_206967_1_.nextDouble() * 0.25D);
		int k = p_206967_4_ & 15;
		int l = p_206967_5_ & 15;

		for (int i1 = p_206967_6_; i1 >= 0; --i1) {
			blockpos$mutable.set(k, i1, l);
			BlockState blockstate2 = p_206967_2_.getBlockState(blockpos$mutable);
			if (blockstate2.isAir()) {
				i = -1;
			} else if (blockstate2.is(p_206967_9_.getBlock())) {
				if (i == -1) {
					if (j <= 0) {
						blockstate = Blocks.AIR.defaultBlockState();
						blockstate1 = p_206967_9_;
					} else if (i1 >= p_206967_14_ - 4 && i1 <= p_206967_14_ + 1) {
						blockstate = p_206967_11_;
						blockstate1 = p_206967_12_;
					}

					if (i1 < p_206967_14_ && (blockstate == null || blockstate.isAir())) {
//						if (p_206967_3_.getTemperature(blockpos$mutable.set(p_206967_4_, i1, p_206967_5_)) < 0.15F) {
//							blockstate = Blocks.ICE.defaultBlockState();
//						} else {
						blockstate = p_206967_10_;
//						}

						blockpos$mutable.set(k, i1, l);
					}

					i = j;
					if (i1 >= p_206967_14_ - 1) {
						p_206967_2_.setBlockState(blockpos$mutable, blockstate, false);
					} else if (i1 < p_206967_14_ - 7 - j) {
						blockstate = Blocks.AIR.defaultBlockState();
						blockstate1 = p_206967_9_;
						p_206967_2_.setBlockState(blockpos$mutable, p_206967_13_, false);
					} else {
						p_206967_2_.setBlockState(blockpos$mutable, blockstate1, false);
					}
				} else if (i > 0) {
					--i;
					p_206967_2_.setBlockState(blockpos$mutable, blockstate1, false);
					if (i == 0 && blockstate1.is(Blocks.SAND) && j > 1) {
						i = p_206967_1_.nextInt(4) + Math.max(0, i1 - 63);
						blockstate1 = blockstate1.is(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.defaultBlockState()
								: Blocks.SANDSTONE.defaultBlockState();
					}
				}
			}
		}

	}
}