package com.machina.world.feature.planet;

import java.util.BitSet;
import java.util.Random;

import com.machina.block.ore.OreType;
import com.machina.planet.attribute.AttributeList;
import com.machina.registration.init.AttributeInit;
import com.machina.registration.init.BlockInit;
import com.machina.util.math.MathUtil;
import com.machina.world.PlanetChunkGenerator;
import com.machina.world.gen.PlanetBlocksGenerator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;

public class PlanetOreFeature extends PlanetBaseFeature {

	private final RegistryObject<? extends Block> base;

	public PlanetOreFeature(AttributeList attr) {
		super(attr);

		this.base = PlanetBlocksGenerator.getBasePalette(attr.getValue(AttributeInit.BASE_BLOCKS)).getBaseReg();
	}

	@Override
	public boolean place(ISeedReader region, PlanetChunkGenerator chunk, Random rand, BlockPos pos) {

		OreType type = OreType.randWeighted(rand);
		BlockState ore = BlockInit.ORE_MAP.get(type).get(base).get().defaultBlockState();

		float f = rand.nextFloat() * (float) Math.PI;
		float f1 = (float) 1f / 8.0F;
		int i = MathHelper.ceil(((float) 1f / 16.0F * 2.0F + 1.0F) / 2.0F);
		double d0 = (double) pos.getX() + Math.sin((double) f) * (double) f1;
		double d1 = (double) pos.getX() - Math.sin((double) f) * (double) f1;
		double d2 = (double) pos.getZ() + Math.cos((double) f) * (double) f1;
		double d3 = (double) pos.getZ() - Math.cos((double) f) * (double) f1;
		int k = pos.getX() - MathHelper.ceil(f1) - i;
		int i1 = pos.getZ() - MathHelper.ceil(f1) - i;
		pos = new BlockPos(pos.getX(),
				MathUtil.randRangeExp(0D, region.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, k, i1), 0.5D, rand),
				pos.getZ());
		int l = pos.getY() - 2 - i;
		double d4 = (double) (pos.getY() + rand.nextInt(3) - 2);
		double d5 = (double) (pos.getY() + rand.nextInt(3) - 2);
		int j1 = 2 * (MathHelper.ceil(f1) + i);
		int k1 = 2 * (2 + i);

		for (int l1 = k; l1 <= k + j1; ++l1) {
			for (int i2 = i1; i2 <= i1 + j1; ++i2) {
				int h1 = region.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, l1, i2);
				if (l <= h1) {
					return this.doPlace(region, rand, base.get(), ore, d0, d1, d2, d3, d4, d5, k, l, i1, j1, k1);
				}
			}
		}

		return false;
	}

	protected boolean doPlace(IWorld world, Random rand, Block bg, BlockState ore, double p_207803_4_,
			double p_207803_6_, double p_207803_8_, double p_207803_10_, double p_207803_12_, double p_207803_14_,
			int p_207803_16_, int p_207803_17_, int p_207803_18_, int p_207803_19_, int p_207803_20_) {
		int i = 0;
		BitSet bitset = new BitSet(p_207803_19_ * p_207803_20_ * p_207803_19_);
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int j = MathUtil.randRange(2, 5, rand);
		double[] adouble = new double[j * 4];

		for (int k = 0; k < j; ++k) {
			float f = (float) k / (float) j;
			double d0 = MathHelper.lerp((double) f, p_207803_4_, p_207803_6_);
			double d2 = MathHelper.lerp((double) f, p_207803_12_, p_207803_14_);
			double d4 = MathHelper.lerp((double) f, p_207803_8_, p_207803_10_);
			double d6 = rand.nextDouble() * (double) j / 16.0D;
			double d7 = ((double) (MathHelper.sin((float) Math.PI * f) + 1.0F) * d6 + 1.0D) / 2.0D;
			adouble[k * 4 + 0] = d0;
			adouble[k * 4 + 1] = d2;
			adouble[k * 4 + 2] = d4;
			adouble[k * 4 + 3] = d7;
		}

		for (int i3 = 0; i3 < j - 1; ++i3) {
			if (!(adouble[i3 * 4 + 3] <= 0.0D)) {
				for (int k3 = i3 + 1; k3 < j; ++k3) {
					if (!(adouble[k3 * 4 + 3] <= 0.0D)) {
						double d12 = adouble[i3 * 4 + 0] - adouble[k3 * 4 + 0];
						double d13 = adouble[i3 * 4 + 1] - adouble[k3 * 4 + 1];
						double d14 = adouble[i3 * 4 + 2] - adouble[k3 * 4 + 2];
						double d15 = adouble[i3 * 4 + 3] - adouble[k3 * 4 + 3];
						if (d15 * d15 > d12 * d12 + d13 * d13 + d14 * d14) {
							if (d15 > 0.0D) {
								adouble[k3 * 4 + 3] = -1.0D;
							} else {
								adouble[i3 * 4 + 3] = -1.0D;
							}
						}
					}
				}
			}
		}

		for (int j3 = 0; j3 < j; ++j3) {
			double d11 = adouble[j3 * 4 + 3];
			if (!(d11 < 0.0D)) {
				double d1 = adouble[j3 * 4 + 0];
				double d3 = adouble[j3 * 4 + 1];
				double d5 = adouble[j3 * 4 + 2];
				int l = Math.max(MathHelper.floor(d1 - d11), p_207803_16_);
				int l3 = Math.max(MathHelper.floor(d3 - d11), p_207803_17_);
				int i1 = Math.max(MathHelper.floor(d5 - d11), p_207803_18_);
				int j1 = Math.max(MathHelper.floor(d1 + d11), l);
				int k1 = Math.max(MathHelper.floor(d3 + d11), l3);
				int l1 = Math.max(MathHelper.floor(d5 + d11), i1);

				for (int i2 = l; i2 <= j1; ++i2) {
					double d8 = ((double) i2 + 0.5D - d1) / d11;
					if (d8 * d8 < 1.0D) {
						for (int j2 = l3; j2 <= k1; ++j2) {
							double d9 = ((double) j2 + 0.5D - d3) / d11;
							if (d8 * d8 + d9 * d9 < 1.0D) {
								for (int k2 = i1; k2 <= l1; ++k2) {
									int l2 = i2 - p_207803_16_ + (j2 - p_207803_17_) * p_207803_19_
											+ (k2 - p_207803_18_) * p_207803_19_ * p_207803_20_;
									if (!bitset.get(l2)) {
										bitset.set(l2);
										blockpos$mutable.set(i2, j2, k2);
										if (bg.equals(world.getBlockState(blockpos$mutable).getBlock())) {
											world.setBlock(blockpos$mutable, ore, 2);
											++i;
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

}
