package com.machina.world.feature.planet;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;

public class PlanetTreeFeature extends PlanetBaseFeature {

	private static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST };

	private static final BlockState LOG = Blocks.OAK_LOG.defaultBlockState();
	private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 2);

	public boolean isAirOrLeaves(IWorldGenerationBaseReader reader, BlockPos pos) {
		if (!(reader instanceof IWorldReader)) {
			return reader.isStateAtPosition(pos, state -> state.isAir() || state.is(BlockTags.LEAVES));
		} else {
			return reader.isStateAtPosition(pos, state -> state.canBeReplacedByLeaves((IWorldReader) reader, pos));
		}
	}

	@Override
	public boolean place(ISeedReader region, ChunkGenerator chunk, Random rand, BlockPos pos) {
		while (pos.getY() > 1 && isAirOrLeaves(region, pos)) {
			pos = pos.below();
		}

		// Trunks
		int height = 4 + rand.nextInt(4);
		if (pos.getY() >= 1 && pos.getY() + 7 + 1 < region.getHeight()) {
			for (int i = pos.getY() + 1; i < pos.getY() + height + 1; i++) {
				region.setBlock(new BlockPos(pos.getX(), i, pos.getZ()), LOG, 3);
			}
		} else {
			return false;
		}

		// Leaves
		for (int i = 0; i < 3; i++) {
			for (Direction d : DIRECTIONS) {
				region.setBlock(new BlockPos(pos.getX(), pos.getY() + height - i, pos.getZ()).relative(d, i), LEAVES,
						3);
			}
		}

		return true;
	}
}