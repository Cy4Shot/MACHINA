package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

public class SDFRound extends SDFUnary {
	private final float radius;

	public SDFRound(SDF source, float radius) {
		super(source);
		this.radius = radius;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		return this.source.getDistance(x, y, z) - radius;
	}
}
