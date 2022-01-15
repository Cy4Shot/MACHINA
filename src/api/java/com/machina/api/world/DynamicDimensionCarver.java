package com.machina.api.world;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.google.common.collect.ImmutableSet;
import com.machina.init.TagInit;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;

// This class is adapted from: https://github.com/TeamTwilight/twilightforest/blob/1.16.x/src/main/java/twilightforest/world/TFCavesCarver.java

public class DynamicDimensionCarver extends WorldCarver<ProbabilityConfig> {

	public DynamicDimensionCarver(Codec<ProbabilityConfig> codec, int maxheight) {
		super(codec, maxheight);
		this.liquids = ImmutableSet.of();
	}

	@Override
	public boolean isStartChunk(Random rand, int chunkX, int chunkZ, ProbabilityConfig config) {
		return rand.nextFloat() <= config.probability;
	}

	@Override
	public boolean carve(IChunk chunk, Function<BlockPos, Biome> biomePos, Random rand, int seaLevel, int chunkXOffset,
			int chunkZOffset, int chunkX, int chunkZ, BitSet carvingMask, ProbabilityConfig config) {
		int size = (this.getRange() * 2 - 1) * 16;
		int maxcaves = rand.nextInt(rand.nextInt(rand.nextInt(45) + 1) + 1);

		for (int k = 0; k < maxcaves; ++k) {
			double randx = chunkXOffset * 16 + rand.nextInt(16);
			double randy = this.getCaveY(rand);
			double randz = chunkZOffset * 16 + rand.nextInt(16);
			int l = 1;
			if (rand.nextInt(4) == 0) {
				float cavesize = 1.0F + rand.nextFloat() * 6.0F;
				this.genLargeNode(chunk, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randx, randy, randz,
						cavesize, 0.5D, carvingMask);
				l += rand.nextInt(4);
			}

			for (int k1 = 0; k1 < l; ++k1) {
				float f = rand.nextFloat() * ((float) Math.PI * 2F);
				float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
				float f2 = this.getThickness(rand);
				int i1 = size - rand.nextInt(size / 4);
				this.genNode(chunk, biomePos, rand.nextLong(), seaLevel, chunkX, chunkZ, randx, randy, randz, f2, f, f3,
						0, i1, this.getYScale(), carvingMask);
			}
		}

		return true;
	}

	protected float getThickness(Random rand) {
		float f = rand.nextFloat() * 2.0F + rand.nextFloat();
		if (rand.nextInt(10) == 0) {
			f *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
		}

		return f;
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected int getCaveY(Random rand) {
		return rand.nextInt(rand.nextInt(120) + 8);
	}

	protected void genLargeNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX,
			int chunkZ, double randOffsetXCoord, double startY, double randOffsetZCoord, float p_227205_14_,
			double p_227205_15_, BitSet carvingMask) {
		double d0 = 1.5D + (double) (MathHelper.sin(((float) Math.PI / 2F)) * p_227205_14_);
		double d1 = d0 * p_227205_15_;
		this.carveSphere(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, randOffsetXCoord + 1.0D, startY,
				randOffsetZCoord, d0, d1, carvingMask);
	}

	protected void genNode(IChunk chunk, Function<BlockPos, Biome> biomePos, long seed, int seaLevel, int chunkX,
			int chunkZ, double randOffsetXCoord, double startY, double randOffsetZCoord, float caveRadius, float pitch,
			float p_227206_16_, int p_227206_17_, int p_227206_18_, double p_227206_19_, BitSet p_227206_21_) {
		Random random = new Random(seed);
		int i = random.nextInt(p_227206_18_ / 2) + p_227206_18_ / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = p_227206_17_; j < p_227206_18_; ++j) {
			double d0 = 1.5D
					+ (double) (MathHelper.sin((float) Math.PI * (float) j / (float) p_227206_18_) * caveRadius);
			double d1 = d0 * p_227206_19_;
			float f2 = MathHelper.cos(p_227206_16_);
			randOffsetXCoord += MathHelper.cos(pitch) * f2;
			startY += MathHelper.sin(p_227206_16_);
			randOffsetZCoord += MathHelper.sin(pitch) * f2;
			p_227206_16_ = p_227206_16_ * (flag ? 0.92F : 0.7F);
			p_227206_16_ = p_227206_16_ + f1 * 0.1F;
			pitch += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && caveRadius > 1.0F) {
				this.genNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, randOffsetXCoord, startY,
						randOffsetZCoord, random.nextFloat() * 0.5F + 0.5F, pitch - ((float) Math.PI / 2F),
						p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_);
				this.genNode(chunk, biomePos, random.nextLong(), seaLevel, chunkX, chunkZ, randOffsetXCoord, startY,
						randOffsetZCoord, random.nextFloat() * 0.5F + 0.5F, pitch + ((float) Math.PI / 2F),
						p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, p_227206_21_);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!this.canReach(chunkX, chunkZ, randOffsetXCoord, randOffsetZCoord, j, p_227206_18_, caveRadius)) {
					return;
				}

				this.carveSphere(chunk, biomePos, seed, seaLevel, chunkX, chunkZ, randOffsetXCoord, startY,
						randOffsetZCoord, d0, d1, p_227206_21_);
			}
		}

	}

	@Override
	protected boolean skip(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
		return p_222708_3_ <= -0.7D
				|| p_222708_1_ * p_222708_1_ + p_222708_3_ * p_222708_3_ + p_222708_5_ * p_222708_5_ >= 1.0D;
	}

	@Override
	protected boolean carveBlock(IChunk chunk, Function<BlockPos, Biome> biomePos, BitSet carvingMask, Random rand,
			BlockPos.Mutable pos, BlockPos.Mutable posUp, BlockPos.Mutable posDown, int p_230358_8_, int p_230358_9_,
			int p_230358_10_, int posX, int posZ, int p_230358_13_, int posY, int p_230358_15_,
			MutableBoolean isSurface) {
		int i = p_230358_13_ | p_230358_15_ << 4 | posY << 8;
		if (carvingMask.get(i)) {
			return false;
		} else {
			carvingMask.set(i);
			pos.set(posX, posY, posZ);
			BlockState blockstate = chunk.getBlockState(pos);
			BlockState blockstate1 = chunk.getBlockState(posUp.setWithOffset(pos, Direction.UP));
			if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.MYCELIUM)) {
				isSurface.setTrue();
			}

			if (!this.canReplaceBlock(blockstate, blockstate1)) {
				return false;
			} else {
				if (posY < 6) {
					return false;
				} else {
					BlockState aboveSurface = chunk.getBlockState(posUp.set(pos.offset(0, 1, 0)));
					for (Direction facing : Direction.values()) {
						BlockState areaAround = chunk.getBlockState(posUp.relative(facing));
						BlockState areaAboveAround = chunk
								.getBlockState(posUp.set(pos.offset(0, 1, 0).relative(facing)));
						if (!aboveSurface.is(Blocks.WATER) && !areaAround.is(Blocks.WATER)
								&& !areaAboveAround.is(Blocks.WATER)) {
							chunk.setBlockState(pos, CAVE_AIR, false);
							if (chunk.getBlockState(pos.above()).is(BlockTags.BASE_STONE_OVERWORLD)
									&& chunk.getBlockState(pos).is(Blocks.CAVE_AIR)) {
								chunk.setBlockState(pos.above(), Blocks.DIRT.defaultBlockState(), false);
							}
							if (isSurface.isTrue()) {
								posDown.setWithOffset(pos, Direction.DOWN);
								if (chunk.getBlockState(posDown).is(Blocks.DIRT)) {
									chunk.setBlockState(posDown, biomePos.apply(pos).getGenerationSettings()
											.getSurfaceBuilderConfig().getTopMaterial(), false);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	protected boolean canReplaceBlock(BlockState state, BlockState aboveState) {
		return this.canReplaceBlock(state)
				|| (state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || state.is(TagInit.Blocks.CARVEABLE_BLOCKS))
						&& !aboveState.getFluidState().is(FluidTags.WATER);
	}
}
