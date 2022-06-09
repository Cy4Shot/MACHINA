package com.machina.util.math;

import net.minecraft.util.Direction;

public class DirectionUtil {

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
}
