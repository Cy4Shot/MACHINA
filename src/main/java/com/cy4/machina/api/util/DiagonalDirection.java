package com.cy4.machina.api.util;

import java.util.function.BiFunction;

import net.minecraft.util.math.BlockPos;

public enum DiagonalDirection {

	NORTH_WEST("north_west", (originalPos, count) -> originalPos.north(count).west(count)),
	NORTH_EAST("north_east", (originalPos, count) -> originalPos.north(count).east(count)),
	
	SOUTH_WEST("south_west", (originalPos, count) -> originalPos.south(count).west(count)),
	SOUTH_EAST("south_east", (originalPos, count) -> originalPos.south(count).east(count));
	
	private BiFunction<BlockPos, Integer, BlockPos> relativeFunc;
	public final String name;
	
	private DiagonalDirection(String name, BiFunction<BlockPos, Integer, BlockPos> relativeFunc) {
		this.relativeFunc = relativeFunc;
		this.name = name;
	}
	
	public BlockPos relative(BlockPos pos, int count) {
		return relativeFunc.apply(pos, count);
	}
	
}
