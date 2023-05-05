package com.machina.util.math.sdf.primitive;

import com.machina.util.math.MathUtil;

public class SDFHexPrism extends SDFPrimitive {
	private float radius;
	private float height;

	public SDFHexPrism setRadius(float radius) {
		this.radius = radius;
		return this;
	}

	public SDFHexPrism setHeight(float height) {
		this.height = height;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float px = Math.abs(x);
		float py = Math.abs(y);
		float pz = Math.abs(z);
		return MathUtil.max(py - height, MathUtil.max((px * 0.866025F + pz * 0.5F), pz) - radius);
	}
}
