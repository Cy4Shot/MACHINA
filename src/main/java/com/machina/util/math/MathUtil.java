package com.machina.util.math;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;

public class MathUtil {

	public static final double TWO_PI = Math.PI * 2;

	public static double distance(Vector2f start, Vector2f end) {
		double a = start.x - end.x;
		double b = start.y - end.y;
		return Math.sqrt(a * a + b * b);
	}

	// https://stackoverflow.com/questions/5036470/automatically-format-a-measurement-into-engineering-units-in-java
	public static final Map<Integer, String> prefixes;
	static {
		Map<Integer, String> tempPrefixes = new HashMap<Integer, String>();
		tempPrefixes.put(0, "");
		tempPrefixes.put(3, "k");
		tempPrefixes.put(6, "M");
		tempPrefixes.put(9, "G");
		tempPrefixes.put(12, "T");
		tempPrefixes.put(-3, "m");
		tempPrefixes.put(-6, "μ");
		prefixes = Collections.unmodifiableMap(tempPrefixes);
	}

	public static String engineering(double val, String unit) {
		if (val == 0)
			return val + unit;

		double tval = val;
		int order = 0;
		while (tval > 1000.0) {
			tval /= 1000.0;
			order += 3;
		}
		while (tval < 1.0) {
			tval *= 1000.0;
			order -= 3;
		}
		return String.format("%.01f", tval) + prefixes.get(order) + unit;
	}
	
	public static float distance(float x1, float x2, float y1, float y2) {
		float dx = Math.abs(x2 - x1);
		float dy = Math.abs(y2 - y1);
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public static int numTrue(boolean... bs) {
		int num = 0;
		for (boolean b : bs) {
			if (b)
				num++;
		}
		return num;
	}

	public static float lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	public static Vector3d lerp(Vector3d a, Vector3d b, float t) {
		return a.add(b.subtract(a).scale(t));
	}

	public static float bezier(float t, Float... f) {
		return (float) (f[0] * (-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
				+ f[1] * (3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t)
				+ f[2] * (-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2)) + f[3] * (Math.pow(t, 3)));
	}

	public static Vector3d bezier(float t, Vector3d... f) {
		return f[0].scale(-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
				.add(f[1].scale(3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t))
				.add(f[2].scale(-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2))).add(f[3].scale(Math.pow(t, 3)));
	}
	
	public static double[] dirichlet(int size, int seed) {
		return new Dirichlet(size).nextDistribution(seed);
	}
	
	public static double clamp(double val, double min, double max) {
	    return Math.max(min, Math.min(max, val));
	}
}
