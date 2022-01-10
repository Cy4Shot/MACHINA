package com.machina.api.util;

import net.minecraft.util.math.vector.Vector2f;

public class MathUtil {
	public static double distance(Vector2f start, Vector2f end) {
		double a = start.x - end.x;
		double b = start.y - end.y;
		return Math.sqrt(a * a + b * b);
	}
}
