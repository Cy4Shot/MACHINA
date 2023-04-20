package com.machina.util.math;

import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class VecUtil {

	public static final Vector3f F3_0 = new Vector3f(0, 0, 0);

	public static Vector3f vec3f(float f) {
		return new Vector3f(f, f, f);
	}

	public static Vector3f norm(Vector3f in) {
		float f = MathHelper.fastInvSqrt(in.x() * in.x() + in.y() * in.y() + in.z() * in.z());
		return new Vector3f(in.x() * f, in.y() * f, in.z() * f);
	}
	
	public static Vector3f randNorm(Random rand) {
		return norm(new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
	}
}
