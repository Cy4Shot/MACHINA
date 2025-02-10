package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;

public class SDFTorus extends SDFPrimitive {
	private final float radiusSmall;
	private final float radiusBig;

	public SDFTorus(float radiusSmall, float radiusBig) {
		this.radiusSmall = radiusSmall;
		this.radiusBig = radiusBig;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float nx = MathUtil.length(x, z) - radiusBig;
		return MathUtil.length(nx, y) - radiusSmall;
	}
}
