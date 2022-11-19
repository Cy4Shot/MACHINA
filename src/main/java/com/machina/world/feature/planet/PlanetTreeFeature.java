package com.machina.world.feature.planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.machina.planet.attribute.PlanetAttributeList;
import com.machina.registration.init.AttributeInit;
import com.machina.util.math.MathUtil;
import com.machina.world.PlanetChunkGenerator;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetBlocksGenerator.BlockPalette;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.PerlinNoiseGenerator;

public class PlanetTreeFeature extends PlanetBaseFeature {

	final float trunkMinHeight;
	final float trunkMaxHeight;
	final float trunkWidthRandomness;
	final float trunkMaxRadius;
	final float trunkMinRadius;
	final float trunkNoiseScale;
	final float leafNoiseScale;
	final float leafStartPercentage;
	final float leafChance;
	final float leafMaxRadius;
	final float leafMinRadius;
	final float leafRadiusDropoff;
	final int leafInterval;
	final int trunkVariationW;
	final int trunkVariationH;
	final int leafLayers;
	final int leavesLayerHeight;
	final int treeType;

	final BlockState LOG;
	final BlockState LEAVES;

	public PlanetTreeFeature(PlanetAttributeList attr) {
		super(attr);

		this.trunkMinHeight = attr.getValue(AttributeInit.TRUNK_MIN_HEIGHT);
		this.trunkMaxHeight = attr.getValue(AttributeInit.TRUNK_MAX_HEIGHT);
		this.trunkWidthRandomness = attr.getValue(AttributeInit.TRUNK_WIDTH_RAND);
		this.trunkMinRadius = attr.getValue(AttributeInit.TRUNK_MIN_RADIUS);
		this.trunkMaxRadius = attr.getValue(AttributeInit.TRUNK_MAX_RADIUS);
		this.trunkNoiseScale = attr.getValue(AttributeInit.TRUNK_NOISE_SCAL);
		this.leafNoiseScale = attr.getValue(AttributeInit.LEAF_NOISE_SCALE);
		this.leafStartPercentage = attr.getValue(AttributeInit.LEAF_START_PRCNT);
		this.leafChance = attr.getValue(AttributeInit.LEAF_SPWN_CHANCE);
		this.leafMinRadius = attr.getValue(AttributeInit.LEAF_LOG_DIS_MIN);
		this.leafMaxRadius = attr.getValue(AttributeInit.LEAF_LOG_DIS_MAX);
		this.leafRadiusDropoff = attr.getValue(AttributeInit.LEAF_LOG_DIS_DRP);
		this.leafInterval = attr.getValue(AttributeInit.LEAF_LAYER_INTER);
		this.trunkVariationH = attr.getValue(AttributeInit.TRUNK_RNDMNESS_H);
		this.trunkVariationW = attr.getValue(AttributeInit.TRUNK_RNDMNESS_W);
		this.leafLayers = attr.getValue(AttributeInit.LEAF_LAYER_COUNT);
		this.leavesLayerHeight = attr.getValue(AttributeInit.LEAF_LAYER_WIDTH);
		this.treeType = attr.getValue(AttributeInit.TREE_TYPE);

		BlockPalette palette = PlanetBlocksGenerator.getTreePalette(attr.getValue(AttributeInit.TREE_BLOCKS));
		this.LOG = palette.getBaseBlock();
		this.LEAVES = palette.getSecondaryBlock();
	}

	@Override
	public boolean place(ISeedReader region, PlanetChunkGenerator chunk, Random rand, BlockPos pos) {
		switch (this.treeType) {
		case 0:
			return placeA(region, chunk, rand, pos);
		case 1:
			return placeB(region, chunk, rand, pos);
		}

		return true;
	}

	public boolean isAirOrLeaves(IWorldGenerationBaseReader reader, BlockPos pos) {
		if (!(reader instanceof IWorldReader)) {
			return reader.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
		} else {
			return reader.isStateAtPosition(pos, state -> state.canBeReplacedByLeaves((IWorldReader) reader, pos));
		}
	}

	public boolean placeA(ISeedReader region, PlanetChunkGenerator gen, Random rand, BlockPos pos) {
		while (pos.getY() > 10 && isAirOrLeaves(region, pos)) {
			pos = pos.below();
		}
		if (pos.getY() < 11)
			return false;

		float trunkHeight = trunkMinHeight + rand.nextFloat() * (trunkMaxHeight - trunkMinHeight);
		float leafRadius = leafMinRadius + rand.nextFloat() * (leafMaxRadius - leafMinRadius);
		float minLeafHeight = trunkHeight * leafStartPercentage;

		// Trunk

		PerlinNoiseGenerator noise = new PerlinNoiseGenerator(new SharedSeedRandom(), IntStream.rangeClosed(5, 15));

		Vector3f p0 = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
		Vector3f p1 = p0.copy();
		Vector3f p2 = p0.copy();
		Vector3f p3 = p0.copy();

		p1.add(0, trunkHeight / 3f, 0);
		p2.add(0, 2f * trunkHeight / 3f, 0);
		p3.add(0, trunkHeight, 0);

		p1.add((rand.nextFloat() - 0.5f) * trunkWidthRandomness, (rand.nextFloat() - 0.5f) * trunkWidthRandomness,
				(rand.nextFloat() - 0.5f) * trunkWidthRandomness);
		p2.add((rand.nextFloat() - 0.5f) * trunkWidthRandomness, (rand.nextFloat() - 0.5f) * trunkWidthRandomness,
				(rand.nextFloat() - 0.5f) * trunkWidthRandomness);

		float r0 = trunkMaxRadius;
		float r1 = trunkMaxRadius + (rand.nextFloat() - 0.5f) * 0.5f;
		float r2 = trunkMinRadius + (rand.nextFloat() - 0.5f) * 0.5f;
		float r3 = trunkMinRadius;

		float x = 0, y = 0, z = 0, r = 0;

		for (float t = 0; t < 1; t += 0.02) {
			x = MathUtil.bezier(t, p0.x(), p1.x(), p2.x(), p3.x());
			y = MathUtil.bezier(t, p0.y(), p1.y(), p2.y(), p3.y());
			z = MathUtil.bezier(t, p0.z(), p1.z(), p2.z(), p3.z());
			r = MathUtil.bezier(t, r0, r1, r2, r3);
			for (float z1 = 0; z1 < 2.0 * r; z1++) {
				for (float x1 = 0; x1 < 2 * r; x1++) {
					float deltaX = r - x1;
					float deltaZ = r - z1;
					double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
					double theta = Math.atan2(z1 - pos.getZ(), x1 - pos.getX());
					double n = noise.getValue(theta, y, false) * trunkNoiseScale;
					if (r - distance + n > 0.9) {
						region.setBlock(new BlockPos(x1 - r + x, y, z1 - r + z), LOG, 3);
					}
				}
			}

			// Leaves
			if (y >= pos.getY() + minLeafHeight) {
				r += leafRadius - (y - pos.getY() - minLeafHeight) / (float) leafInterval * leafRadiusDropoff
						- ((y - pos.getY() - minLeafHeight) % (float) leafInterval) / (float) leafInterval;
				for (float z1 = 0; z1 < 2.0 * r; z1++) {
					for (float x1 = 0; x1 < 2 * r; x1++) {
						float deltaX = r - x1;
						float deltaZ = r - z1;
						double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
						double theta = Math.atan2(z1 - pos.getZ(), x1 - pos.getX());
						double n = noise.getValue(theta, y, false) * leafNoiseScale;
						if (r - distance + n > 0.9) {
							if (rand.nextFloat() < leafChance)
								region.setBlock(new BlockPos(x1 - r + x, y, z1 - r + z), LEAVES, 3);
						}
					}
				}
			}
		}

		while (r - r3 > 0) {
			y++;
			r = r3 + leafRadius - (y - pos.getY() - minLeafHeight) / (float) leafInterval * leafRadiusDropoff
					- ((y - pos.getY() - minLeafHeight) % (float) leafInterval) / (float) leafInterval;
			for (float z1 = 0; z1 < 2.0 * r; z1++) {
				for (float x1 = 0; x1 < 2 * r; x1++) {
					float deltaX = r - x1;
					float deltaZ = r - z1;
					double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
					double theta = Math.atan2(z1 - pos.getZ(), x1 - pos.getX());
					double n = noise.getValue(theta, y, false) * leafNoiseScale;
					if (r - distance + n > 0.9) {
						if (rand.nextFloat() < leafChance)
							region.setBlock(new BlockPos(x1 - r + x, y, z1 - r + z), LEAVES, 3);
					}
				}
			}
		}

		return true;
	}

	BiFunction<ISeedReader, BlockPos, Boolean> replace = (world,
			pos) -> world.getBlockState(pos).canBeReplacedByLeaves(world, pos)
					|| world.getBlockState(pos).getBlock().is(BlockTags.SAPLINGS)
					|| world.getBlockState(pos).getBlock() == Blocks.VINE
					|| world.getBlockState(pos).getBlock() instanceof BushBlock;

	public boolean placeB(ISeedReader world, PlanetChunkGenerator chunk, Random rand, BlockPos pos) {

		PerlinNoiseGenerator noise = new PerlinNoiseGenerator(new SharedSeedRandom(), IntStream.rangeClosed(5, 15));

		float trunkHeight = trunkMinHeight + rand.nextFloat() * (trunkMaxHeight - trunkMinHeight);
		float leafRadius = leafMinRadius + rand.nextFloat() * (leafMaxRadius - leafMinRadius);

		while (pos.getY() > 1 && isAirOrLeaves(world, pos)) {
			pos = pos.below();
		}

		if (pos.getY() < 256 - trunkHeight - 1) {
			List<BlockPos> trunk = generateTrunk(world, pos, (int) trunkHeight, rand, trunkVariationW, trunkVariationH);

			for (BlockPos trunkEnd : trunk) {
				addLeaves(world, rand, trunkEnd, leafLayers, (int) leafRadius, leavesLayerHeight, leafNoiseScale,
						leafChance, noise);
			}

			return true;
		}

		return false;
	}

	protected void addLeaves(ISeedReader world, Random rand, BlockPos trunkEnd, int leavesLayers, int maxLeafRadius,
			int leavesLayerHeight, float leafNoiseScale, float leafChance, PerlinNoiseGenerator noise) {
		for (int y = trunkEnd.getY() - leavesLayers; y <= trunkEnd.getY(); y++) {
			int currentLayer = y - (trunkEnd.getY());
			int leavesRadius = maxLeafRadius - currentLayer / leavesLayerHeight;

			for (float z1 = 0; z1 < 2.0 * leavesRadius; z1++) {
				for (float x1 = 0; x1 < 2 * leavesRadius; x1++) {
					float deltaX = leavesRadius - x1;
					float deltaZ = leavesRadius - z1;
					double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
					double theta = Math.atan2(z1 - trunkEnd.getZ(), x1 - trunkEnd.getX());
					double n = noise.getValue(theta, currentLayer, false) * leafNoiseScale;
					if (leavesRadius - distance + n > 0.9) {
						if (rand.nextFloat() < leafChance)
							world.setBlock(new BlockPos(x1 - leavesRadius + trunkEnd.getX(), y,
									z1 - leavesRadius + trunkEnd.getZ()), LEAVES, 3);
					}
				}
			}
		}
	}

	protected List<BlockPos> generateTrunk(ISeedReader world, BlockPos start, int height, Random rand,
			int trunkVariationW, int trunkVariationH) {
		List<BlockPos> pos = new ArrayList<BlockPos>();
		pos.add(start.above(height));
		placeLogs(world, start, start.above(height));

		for (int i = 0; i < 3; i++) {
			int offX = rand.nextInt(2 * trunkVariationW) - trunkVariationW;
			int offY = rand.nextInt(2 * trunkVariationH) - trunkVariationH;
			int offZ = rand.nextInt(2 * trunkVariationW) - trunkVariationW;
			BlockPos trunk = start.above(height).offset(offX, offY, offZ);
			placeLogs(world, start, trunk);
			pos.add(trunk);
		}

		return pos;
	}

	protected void placeLogs(ISeedReader world, BlockPos start, BlockPos end) {

		world.setBlock(start, LOG, 3);

		int x1 = start.getX();
		int y1 = start.getY();
		int z1 = start.getZ();
		int x2 = end.getX();
		int y2 = end.getY();
		int z2 = end.getZ();
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int dz = Math.abs(z2 - z1);
		int xs = x2 > x1 ? 1 : -1;
		int ys = y2 > y1 ? 1 : -1;
		int zs = z2 > z1 ? 1 : -1;

		if (dx >= dy && dx >= dz) {
			int p1 = 2 * dy - dx;
			int p2 = 2 * dz - dx;
			while (x1 != x2) {
				x1 += xs;
				if (p1 >= 0) {
					y1 += ys;
					p1 -= 2 * dx;
				}
				if (p2 >= 0) {
					z1 += zs;
					p2 -= 2 * dx;
				}
				p1 += 2 * dy;
				p2 += 2 * dz;
				world.setBlock(new BlockPos(x1, y1, z1), LOG, 3);
			}
		} else if (dy >= dx && dy >= dz) {
			int p1 = 2 * dx - dy;
			int p2 = 2 * dz - dy;
			while (y1 != y2) {
				y1 += ys;
				if (p1 >= 0) {
					x1 += xs;
					p1 -= 2 * dy;
				}
				if (p2 >= 0) {
					z1 += zs;
					p2 -= 2 * dy;
				}
				p1 += 2 * dx;
				p2 += 2 * dz;
				world.setBlock(new BlockPos(x1, y1, z1), LOG, 3);
			}
		} else {
			int p1 = 2 * dy - dz;
			int p2 = 2 * dx - dz;
			while (z1 != z2) {
				z1 += zs;
				if (p1 >= 0) {
					y1 += ys;
					p1 -= 2 * dz;
				}
				if (p2 >= 0) {
					x1 += xs;
					p2 -= 2 * dz;
				}
				p1 += 2 * dx;
				p2 += 2 * dz;
				world.setBlock(new BlockPos(x1, y1, z1), LOG, 3);
			}
		}
	}

	public boolean placeLeaves(ISeedReader world, BlockPos pos) {
		if (this.replace.apply(world, pos)) {
			world.setBlock(pos, LEAVES, 3);
			return true;
		}
		return false;
	}
}
