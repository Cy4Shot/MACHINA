package com.machina.util.math;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.math.vector.Vector2f;

public class MathUtil {
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

	public static int numTrue(boolean... bs) {
		int num = 0;
		for (boolean b : bs) {
			if (b)
				num++;
		}
		return num;
	}
}
