package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

public class SDFScale3D extends SDFUnary {
	private final float x;
	private final float y;
	private final float z;

	public SDFScale3D(SDF source, float x, float y, float z) {
		super(source);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x / this.x, y / this.y, z / this.z);
	}
}
