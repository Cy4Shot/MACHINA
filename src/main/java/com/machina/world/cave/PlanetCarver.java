package com.machina.world.cave;

import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.machina.registration.init.TagInit;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
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

	protected void genRoom(IChunk chunk, Function<BlockPos, Biome> biome, long seed, int sea, int cX, int cZ, double rX,
			double rY, double rZ, float size, double yScale, BitSet bitset) {
		double d0 = 1.5D + (double) (MathHelper.sin(((float) Math.PI / 2F)) * size);
		this.carveSphere(chunk, biome, seed, sea, cX, cZ, rX + 1.0D, rY, rZ, d0, d0 * yScale, bitset);
	}

	protected void genTunnel(IChunk chunk, Function<BlockPos, Biome> biome, long seed, int sea, int cX, int cZ,
			double rX, double rY, double rZ, float thickness, float rA, float rB, int start, int end, double yScale,
			BitSet bitset) {
		Random random = new Random(seed);
		int i = random.nextInt(end / 2) + end / 4;
		boolean flag = random.nextInt(6) == 0;
		float f = 0.0F;
		float f1 = 0.0F;

		for (int j = start; j < end; ++j) {
			double d0 = 1.5D + (double) (MathHelper.sin((float) Math.PI * (float) j / (float) end) * thickness);
			float f2 = MathHelper.cos(rB);
			rX += (double) (MathHelper.cos(rA) * f2);
			rY += (double) MathHelper.sin(rB);
			rZ += (double) (MathHelper.sin(rA) * f2);
			rB *= (flag ? 0.92F : 0.7F);
			rB += f1 * 0.1F;
			rA += f * 0.1F;
			f1 *= 0.9F;
			f *= 0.75F;
			f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
			if (j == i && thickness > 1.0F) {
				this.genTunnel(chunk, biome, random.nextLong(), sea, cX, cZ, rX, rY, rZ,
						random.nextFloat() * 0.5F + 0.5F, rA - ((float) Math.PI / 2F), rB / 3.0F, j, end, 1.0D, bitset);
				this.genTunnel(chunk, biome, random.nextLong(), sea, cX, cZ, rX, rY, rZ,
						random.nextFloat() * 0.5F + 0.5F, rA + ((float) Math.PI / 2F), rB / 3.0F, j, end, 1.0D, bitset);
				return;
			}

			if (random.nextInt(4) != 0) {
				if (!this.canReach(cX, cZ, rX, rZ, j, end, thickness)) {
					return;
				}

				this.carveSphere(chunk, biome, seed, sea, cX, cZ, rX, rY, rZ, d0, d0 * yScale, bitset);
			}
		}

	}

	@Override
	protected boolean carveBlock(IChunk chunk, Function<BlockPos, Biome> biome, BitSet bitset, Random rand,
			BlockPos.Mutable pos, BlockPos.Mutable pos1, BlockPos.Mutable _p, int _1, int _2, int _3, int cX, int cY,
			int x, int y, int z, MutableBoolean dirt) {
		int i = x | z << 4 | y << 8;

		if (!bitset.get(i)) {
			bitset.set(i);
			pos.set(cX, y, cY);
			BlockState state = chunk.getBlockState(pos);
			BlockState above = chunk.getBlockState(pos1.setWithOffset(pos, Direction.UP));

			if (this.canReplaceBlock(state, above)) {
				chunk.setBlockState(pos, CAVE_AIR, false);
				return true;
			}
		}

		return false;
	}

	@Override
	protected boolean skip(double x, double y, double z, int a) {
		return y <= -0.7D || x * x + y * y + z * z >= 1.0D;
	}

	@Override
	protected boolean canReplaceBlock(BlockState state, BlockState aboveState) {
		return this.canReplaceBlock(state)
				|| state.is(TagInit.Blocks.CARVEABLE_BLOCKS) && !aboveState.getFluidState().is(FluidTags.WATER);
	}
}