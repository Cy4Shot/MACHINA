package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

public class SDFInvert extends SDFUnary {
	public SDFInvert(SDF source) {
		super(source);
	}

	@Override
	public float getDistance(float x, float y, float z) {
		return -this.source.getDistance(x, y, z);
	}
}
