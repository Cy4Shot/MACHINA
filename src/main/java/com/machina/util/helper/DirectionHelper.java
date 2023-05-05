package com.machina.util.helper;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.Direction;

public class DirectionHelper {

	public static final List<Direction> HORIZONTAL = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH,
			Direction.WEST);

	public static int toYaw(Direction d) {
		switch (d) {
		case EAST:
			return -90;
		case NORTH:
			return -180;
		case WEST:
			return 90;
		default:
			return 0;
		}
	}
}
