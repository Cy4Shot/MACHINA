package com.machina.api.util.math.sdf.operator;

import org.joml.Vector3f;

import com.machina.api.util.math.sdf.SDF;

import net.minecraft.util.RandomSource;

public class SDFDirectionalDisplacement extends SDFUnary {

	private final RandomSource random;
	private final float max;
	private final Vector3f axisMask;

	private final float capY; // Y position of the flat top

	public SDFDirectionalDisplacement(SDF source, RandomSource random, float max, Vector3f axisMask, float capY) {
		super(source);
		this.random = random;
		this.max = max;
		this.axisMask = new Vector3f(axisMask);
		this.capY = capY;
	}

	@Override
	public float getDistance(float x, float y, float z) {

		// ABOVE OR ON THE CAP → NO DISPLACEMENT
		if (y >= capY) {
			return source.getDistance(x, y, z);
		}

		float dx = (random.nextFloat() - 0.5f) * max * axisMask.x;
		float dz = (random.nextFloat() - 0.5f) * max * axisMask.z;

		return source.getDistance(x + dx, y, z + dz);
	}
}
