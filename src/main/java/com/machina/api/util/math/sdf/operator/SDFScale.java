package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

public class SDFScale extends SDFUnary {
	private final float scale;

	public SDFScale(SDF source, float scale) {
		super(source);
		this.scale = scale;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		return source.getDistance(x / scale, y / scale, z / scale) * scale;
	}
}
