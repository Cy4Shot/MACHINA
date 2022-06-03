package com.machina.world.carver;

import com.google.common.collect.ImmutableSet;
import com.machina.registration.init.TagInit;
import com.machina.util.math.OpenSimplex2F;
import com.machina.util.server.BlockUtils;
import com.machina.world.DynamicDimensionChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

public class CarverDecorator {

	public static final ImmutableSet<Direction> dirs = ImmutableSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST, Direction.UP, Direction.DOWN);

	public static void decorateCavesAt(IChunk chunk, BlockPos pos, DynamicDimensionChunkGenerator gen) {
		BlockPos.Mutable adjecent = new BlockPos.Mutable();

		for (Direction dir : dirs) {
			adjecent.set(pos).move(dir);

			if (canGenSide(chunk, chunk.getBlockState(adjecent), dir)) {
				switch (dir) {
				case UP:
					// Gen Ceil

					break;
				case DOWN:
					// Gen Floor

					final double d = getNoise(gen.getCaveDecoNoise(), adjecent, 0.125d);
					if (d < -0.3d)
						chunk.setBlockState(adjecent, gen.baseBlocks.getSecondaryBlock(), false);

					break;
				default:
					// Gen Wall

					break;
				}
			}
		}

		for (Direction dir : dirs) {
			adjecent.set(pos).move(dir);
			if (canGenExtra(chunk, chunk.getBlockState(pos), chunk.getBlockState(adjecent), dir)) {
				switch (dir) {
				case UP:
					// Gen Ceil Extra

					break;
				case DOWN:
					// Gen Floor Extra

					break;
				default:
					// Gen Wall Extra

					if (dir == Direction.EAST && adjecent.getX() % 16 == 0)
						break;
					if (dir == Direction.SOUTH && adjecent.getZ() % 16 == 0)
						break;
					if (dir == Direction.WEST && (adjecent.getX() + 1) % 16 == 0)
						break;
					if (dir == Direction.NORTH && (adjecent.getZ() + 1) % 16 == 0)
						break;

					genSlope(chunk, pos, dir, gen);
					break;
				}
			}
		}

		// Fill
		if (canGenFill(chunk, chunk.getBlockState(pos))) {

		}
	}

	public static void genSlope(IChunk world, BlockPos pos, Direction wallDir, DynamicDimensionChunkGenerator gen) {

		// Initialize
		BlockPos.Mutable mutPos = new BlockPos.Mutable().set(pos);
		mutPos.set(pos).move(0, -1, 0);
		final boolean isDown = world.getBlockState(mutPos.set(pos).move(0, -1, 0)).is(TagInit.Blocks.CARVEABLE_BLOCKS);
		final boolean isUp = world.getBlockState(mutPos.set(pos).move(0, 1, 0)).is(TagInit.Blocks.CARVEABLE_BLOCKS);
		if (!isDown && !isUp)
			return;
		mutPos.set(pos);

		// Chance to generate
		int air = 0;
		Direction oppDir = wallDir.getOpposite();
		while (air < 16 && !world.getBlockState(mutPos.move(oppDir)).isFaceSturdy(world, mutPos, wallDir))
			++air;
		int chance = 4;
		if (air <= 3)
			chance = 2;
		if (gen.random.nextInt(10) >= chance)
			return;

		// Generate
		if (gen.random.nextInt(5) <= 2) {
			genBlock(world, pos,
					BlockUtils.waterlog(
							gen.baseBlocks.getStairBlock().setValue(BlockStateProperties.HORIZONTAL_FACING, wallDir)
									.setValue(BlockStateProperties.HALF, isDown ? Half.BOTTOM : Half.TOP),
							world, pos));
		} else {
			genBlock(world, pos, BlockUtils.waterlog(gen.baseBlocks.getSlabBlock()
					.setValue(BlockStateProperties.SLAB_TYPE, isDown ? SlabType.BOTTOM : SlabType.TOP), world, pos));
		}
	}

	public static boolean genBlock(IChunk world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, false);
		return true;
	}

	public static double getNoise(OpenSimplex2F noise, BlockPos pos, double frequency) {
		return noise.noise3_XZBeforeY((double) pos.getX() * frequency, (double) pos.getY() * frequency,
				(double) pos.getZ() * frequency);
	}

	public static boolean canGenSide(IChunk chunk, BlockState state, Direction dir) {
		return state.is(TagInit.Blocks.CARVEABLE_BLOCKS);
	}

	@SuppressWarnings("deprecation")
	public static boolean canGenExtra(IChunk chunk, BlockState state, BlockState sState, Direction dir) {
		return state.isAir() && (sState.getMaterial() == Material.WOOD || sState.is(TagInit.Blocks.CARVEABLE_BLOCKS));
	}

	@SuppressWarnings("deprecation")
	public static boolean canGenFill(IChunk chunk, BlockState state) {
		return state.isAir();
	}

}
