package com.machina.world.gen;

import com.machina.registration.init.TagInit;
import com.machina.util.helper.BlockHelper;
import com.machina.util.math.OpenSimplex2F;
import com.machina.world.PlanetChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;

// https://github.com/Melonslise/Subterranean-Wilderness/blob/84d3b6ffe4c268dfa73b5d066fa69b31a7f5107c/src/main/java/melonslise/subwild/common/world/gen/feature/cavetype/BasicCaveType.java#L94
public class PlanetSlopeGenerator {

	public static void decorateAt(IChunk chunk, BlockPos pos, PlanetChunkGenerator gen,
			boolean allowVerticalConnections) {
		for (Direction dir : Direction.values()) {

			BlockPos adjecent = pos.relative(dir);

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

		for (Direction dir : Direction.values()) {
			BlockPos adjecent = pos.relative(dir);
			if (canGenExtra(chunk.getBlockState(pos), chunk.getBlockState(adjecent))) {
				switch (dir) {
				case UP:
					// Gen Ceil Extra

					break;
				case DOWN:
					// Gen Floor Extra

					break;
				default:

					if (dir == Direction.EAST && adjecent.getX() % 16 == 0)
						break;
					if (dir == Direction.SOUTH && adjecent.getZ() % 16 == 0)
						break;
					if (dir == Direction.WEST && (adjecent.getX() + 1) % 16 == 0)
						break;
					if (dir == Direction.NORTH && (adjecent.getZ() + 1) % 16 == 0)
						break;

					genSlope(chunk, pos, dir, gen, allowVerticalConnections);
					break;
				}
			}
		}
	}

	public static void genSlope(IChunk world, BlockPos pos, Direction wallDir, PlanetChunkGenerator gen,
			boolean allowVerticalConnections) {

		BlockPos.Mutable mutPos = new BlockPos.Mutable().set(pos);

		mutPos.set(pos).move(0, -1, 0);
		final boolean isDown = world.getBlockState(mutPos).is(TagInit.Blocks.CARVEABLE_BLOCKS);
		mutPos.set(pos).move(0, 1, 0);
		final boolean isUp = world.getBlockState(mutPos).is(TagInit.Blocks.CARVEABLE_BLOCKS);
		if (!isDown && !isUp)
			return;

		if (!allowVerticalConnections) {
			if (world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.east()).isAir()
					&& world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.west()).isAir())
				return;
		}

		mutPos.set(pos);
		int air = 0;
		Direction oppDir = wallDir.getOpposite();
		while (air < 16 && !world.getBlockState(mutPos.move(oppDir)).isFaceSturdy(world, mutPos, wallDir))
			++air;

		int chance = 4;
		if (air <= 3)
			chance = 2;
		if (gen.random.nextInt(10) >= chance)
			return;
		if (gen.random.nextInt(5) <= 2)
			genBlock(world, pos,
					BlockHelper.waterlog(
							gen.baseBlocks.getStairBlock().setValue(BlockStateProperties.HORIZONTAL_FACING, wallDir)
									.setValue(BlockStateProperties.HALF, isDown ? Half.BOTTOM : Half.TOP),
							world, pos));
		else
			genBlock(world, pos, BlockHelper.waterlog(gen.baseBlocks.getSlabBlock()
					.setValue(BlockStateProperties.SLAB_TYPE, isDown ? SlabType.BOTTOM : SlabType.TOP), world, pos));
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

	public static boolean canGenExtra(BlockState state, BlockState sState) {
		return state.isAir() && sState.is(TagInit.Blocks.CARVEABLE_BLOCKS);
	}
}
