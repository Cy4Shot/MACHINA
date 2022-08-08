package com.machina.world.feature.planet;

import java.util.Random;
import java.util.stream.IntStream;

import com.machina.util.math.MathUtil;
import com.machina.world.PlanetChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.PerlinNoiseGenerator;

public class PlanetTreeFeature extends PlanetBaseFeature {

	private static final BlockState LOG = Blocks.OAK_LOG.defaultBlockState();
	private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT,
			true);

	public boolean isAirOrLeaves(IWorldGenerationBaseReader reader, BlockPos pos) {
		if (!(reader instanceof IWorldReader)) {
			return reader.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
		} else {
			return reader.isStateAtPosition(pos, state -> state.canBeReplacedByLeaves((IWorldReader) reader, pos));
		}
	}

	@Override
	public boolean place(ISeedReader region, PlanetChunkGenerator gen, Random rand, BlockPos pos) {
		while (pos.getY() > 1 && isAirOrLeaves(region, pos)) {
			pos = pos.below();
		}

		float trunkMinHeight = 15;
		float trunkMaxHeight = 25;
		float trunkWidthRandomness = 10f;
		float trunkMaxRadius = 3;
		float trunkMinRadius = 2;
		float trunkNoiseScale = 0.5f;
		float leafNoiseScale = 0.5f;
		float leafStartPercentage = 0.5f;
		float leafChance = 0.8f;
		float leafMaxRadius = 5;
		float leafMinRadius = 2;
		float leafRadiusDropoff = 0.7f;
		int leafInterval = 4;

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

	public enum LeafType {
		LAYERS
	}
}