package com.machina.util.math;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;

public class DirectionUtil {

	public static final List<Direction> DIRECTIONS = Lists.newArrayList(Direction.values());
	
	public static final List<Direction> HORIZONTAL = Lists.newArrayList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

	public static int rotations(Direction dir) {
		switch (dir) {
		case EAST:
			return 3;
		case NORTH:
			return 2;
		case WEST:
			return 1;
		default:
			return 0;
		}
	}

	// (NORTH => EAST => SOUTH => WEST => NORTH)
	public static Direction rotate(Direction a, Direction b) {
		if (a.getAxis().equals(Direction.Axis.Y) || b.getAxis().equals(Direction.Axis.Y))
			return a;
		for (int i = 0; i < rotations(b); i++)
			a = a.getClockWise();
		return a;
	}

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
	
	public static Rotation fixHorizontal(Rotation rot) {
		switch (rot) {
		case CLOCKWISE_90:
			return Rotation.COUNTERCLOCKWISE_90;
		case COUNTERCLOCKWISE_90:
			return Rotation.CLOCKWISE_90;
		default:
			return rot;
		}
	}
}
