package com.machina.world.cave;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.machina.registration.init.TagInit;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.WorldCarver;

public class PlanetCarver extends WorldCarver<PlanetCarverConfig> {

	public static Supplier<ConfiguredCarver<?>> create(final float chance, final int length, final float thickness) {
		return () -> new PlanetCarver(PlanetCarverConfig.CODEC, 256)
				.configured(new PlanetCarverConfig(chance, length, thickness));
	}

	public PlanetCarver(Codec<PlanetCarverConfig> config, int height) {
		super(config, height);
	}

	public boolean isStartChunk(Random rand, int chunkX, int chunkZ, PlanetCarverConfig config) {
		return rand.nextFloat() <= config.probability;
	}

	public boolean carve(IChunk chunk, Function<BlockPos, Biome> biome, Random rand, int sea, int cXOffset,
			int cZOffset, int cX, int cZ, BitSet bitset, PlanetCarverConfig config) {
		int size = (this.getRange(config) * 2 - 1) * 16;
		int maxcaves = rand.nextInt(rand.nextInt(rand.nextInt(this.getCaveBound()) + 1) + 1);

		for (int k = 0; k < maxcaves; ++k) {
			double randX = (double) (cXOffset * 16 + rand.nextInt(16));
			double randY = (double) this.getCaveY(rand);
			double randZ = (double) (cZOffset * 16 + rand.nextInt(16));
			int l = 1;
			if (rand.nextInt(4) == 0) {
				float caveSize = 1.0F + rand.nextFloat() * 6.0F;
				this.genRoom(chunk, biome, rand.nextLong(), sea, cX, cZ, randX, randY, randZ, caveSize, 0.5D, bitset);
				l += rand.nextInt(4);
			}

			for (int k1 = 0; k1 < l; ++k1) {
				float f = rand.nextFloat() * ((float) Math.PI * 2F);
				float f3 = (rand.nextFloat() - 0.5F) / 4.0F;
				float f2 = this.getThickness(rand, config);
				int i1 = size - rand.nextInt(size / 4);
				this.genTunnel(chunk, biome, rand.nextLong(), sea, cX, cZ, randX, randY, randZ, f2, f, f3, 0, i1,
						this.getYScale(), bitset);
			}
		}

		return true;
	}

	public int getRange(PlanetCarverConfig c) {
		return c.length;
	}

	protected int getCaveBound() {
		return 15;
	}

	protected float getThickness(Random r, PlanetCarverConfig c) {
		float f = r.nextFloat() * c.thickness + r.nextFloat();
		if (r.nextInt(10) == 0) {
			f *= r.nextFloat() * r.nextFloat() * 3.0F + 1.0F;
		}

		return f * 4; // Global thickness multiplier :)
	}

	protected double getYScale() {
		return 1.0D;
	}

	protected int getCaveY(Random r) {
		return r.nextInt(r.nextInt(120) + 8);
	}

	protected void genRoom(IChunk chunk, Function<BlockPos, Biome> biome, long seed, int p_227205_5_, int p_227205_6_,
			int p_227205_7_, double p_227205_8_, double p_227205_10_, double p_227205_12_, float p_227205_14_,
			double p_227205_15_, BitSet bitset) {
		double d0 = 1.5D + (double) (MathHelper.sin(((float) Math.PI / 2F)) * p_227205_14_);
		double d1 = d0 * p_227205_15_;
		this.carveSphere(chunk, biome, seed, p_227205_5_, p_227205_6_, p_227205_7_, p_227205_8_ + 1.0D, p_227205_10_,
				p_227205_12_, d0, d1, bitset);
	}

	protected void genTunnel(IChunk chunk, Function<BlockPos, Biome> biome, long seed, int p_227206_5_, int p_227206_6_,
			int p_227206_7_, double p_227206_8_, double p_227206_10_, double p_227206_12_, float p_227206_14_,
			float p_227206_15_, float p_227206_16_, int p_227206_17_, int p_227206_18_, double p_227206_19_,
			BitSet bitset) {
		Random random = new Random(seed);
		int i = random.nextInt(p_227206_18_ / 2) + p_227206_18_ / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = p_227206_17_; j < p_227206_18_; ++j) {
			double d0 = 1.5D
					+ (double) (MathHelper.sin((float) Math.PI * (float) j / (float) p_227206_18_) * p_227206_14_);
			double d1 = d0 * p_227206_19_;
			float f2 = MathHelper.cos(p_227206_16_);
			p_227206_8_ += (double) (MathHelper.cos(p_227206_15_) * f2);
			p_227206_10_ += (double) MathHelper.sin(p_227206_16_);
			p_227206_12_ += (double) (MathHelper.sin(p_227206_15_) * f2);
			p_227206_16_ = p_227206_16_ * (flag ? 0.92F : 0.7F);
			p_227206_16_ = p_227206_16_ + f1 * 0.1F;
			p_227206_15_ += f * 0.1F;
			f1 = f1 * 0.9F;
			f = f * 0.75F;
			f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && p_227206_14_ > 1.0F) {
				this.genTunnel(chunk, biome, random.nextLong(), p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_,
						p_227206_10_, p_227206_12_, random.nextFloat() * 0.5F + 0.5F,
						p_227206_15_ - ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, bitset);
				this.genTunnel(chunk, biome, random.nextLong(), p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_,
						p_227206_10_, p_227206_12_, random.nextFloat() * 0.5F + 0.5F,
						p_227206_15_ + ((float) Math.PI / 2F), p_227206_16_ / 3.0F, j, p_227206_18_, 1.0D, bitset);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!this.canReach(p_227206_6_, p_227206_7_, p_227206_8_, p_227206_12_, j, p_227206_18_,
						p_227206_14_)) {
					return;
				}

				this.carveSphere(chunk, biome, seed, p_227206_5_, p_227206_6_, p_227206_7_, p_227206_8_, p_227206_10_,
						p_227206_12_, d0, d1, bitset);
			}
		}

	}

	@Override
	protected boolean carveBlock(IChunk p_230358_1_, Function<BlockPos, Biome> p_230358_2_, BitSet p_230358_3_,
			Random p_230358_4_, BlockPos.Mutable p_230358_5_, BlockPos.Mutable p_230358_6_,
			BlockPos.Mutable p_230358_7_, int p_230358_8_, int p_230358_9_, int p_230358_10_, int p_230358_11_,
			int p_230358_12_, int p_230358_13_, int p_230358_14_, int p_230358_15_, MutableBoolean p_230358_16_) {
		int i = p_230358_13_ | p_230358_15_ << 4 | p_230358_14_ << 8;
		if (p_230358_3_.get(i)) {
			return false;
		} else {
			p_230358_3_.set(i);
			p_230358_5_.set(p_230358_11_, p_230358_14_, p_230358_12_);
			BlockState blockstate = p_230358_1_.getBlockState(p_230358_5_);
			BlockState blockstate1 = p_230358_1_.getBlockState(p_230358_6_.setWithOffset(p_230358_5_, Direction.UP));
			if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.MYCELIUM)) {
				p_230358_16_.setTrue();
			}

			if (!this.canReplaceBlock(blockstate, blockstate1)) {
				return false;
			} else {
				if (p_230358_14_ < 11) {
					p_230358_1_.setBlockState(p_230358_5_, LAVA.createLegacyBlock(), false);
				} else {
					p_230358_1_.setBlockState(p_230358_5_, CAVE_AIR, false);
					if (p_230358_16_.isTrue()) {
						p_230358_7_.setWithOffset(p_230358_5_, Direction.DOWN);
						if (p_230358_1_.getBlockState(p_230358_7_).is(Blocks.DIRT)) {
							p_230358_1_.setBlockState(p_230358_7_, p_230358_2_.apply(p_230358_5_)
									.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial(), false);
						}
					}
				}

				return true;
			}
		}
	}

	protected boolean skip(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
		return p_222708_3_ <= -0.7D
				|| p_222708_1_ * p_222708_1_ + p_222708_3_ * p_222708_3_ + p_222708_5_ * p_222708_5_ >= 1.0D;
	}

	@Override
	protected boolean canReplaceBlock(BlockState state, BlockState aboveState) {
		return this.canReplaceBlock(state)
				|| (state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || state.is(TagInit.Blocks.CARVEABLE_BLOCKS))
						&& !aboveState.getFluidState().is(FluidTags.WATER);
	}

}
