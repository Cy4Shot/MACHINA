package com.machina.api.util.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.world.phys.Vec3;

public class MathUtil {

	public static final Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
	public static final Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
	public static final Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
	public static final Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
	public static final Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
	public static final Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);

	public static final double TWO_PI = Math.PI * 2;

	public static float lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	public static Vec3 lerp(Vec3 a, Vec3 b, float t) {
		return a.add(b.subtract(a).scale(t));
	}

	public static float bezier(float t, Float... f) {
		return (float) (f[0] * (-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
				+ f[1] * (3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t)
				+ f[2] * (-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2)) + f[3] * (Math.pow(t, 3)));
	}

	public static Vec3 bezier(float t, Vec3... f) {
		return f[0].scale(-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1)
				.add(f[1].scale(3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 3 * t))
				.add(f[2].scale(-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2))).add(f[3].scale(Math.pow(t, 3)));
	}

	public static double positiveModulo(double pNumerator, double pDenominator) {
		return (pNumerator % pDenominator + pDenominator) % pDenominator;
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}

	public static Quaternionf rotationDegrees(Vector3f vec, float degrees) {
		degrees *= ((float) Math.PI / 180F);

		float f = sin(degrees / 2.0F);
		float x = vec.x() * f;
		float y = vec.y() * f;
		float z = vec.z() * f;
		float w = cos(degrees / 2.0F);
		return new Quaternionf(x, y, z, w);
	}

	private static float cos(float pAngle) {
		return (float) Math.cos((double) pAngle);
	}

	private static float sin(float pAngle) {
		return (float) Math.sin((double) pAngle);
	}

	public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);

		LinkedHashMap<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
}