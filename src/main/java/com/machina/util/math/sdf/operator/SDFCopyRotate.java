package com.machina.util.math.sdf.operator;

import com.machina.util.math.MathUtil;

public class SDFCopyRotate extends SDFUnary {
	int count = 1;

	public SDFCopyRotate setCount(int count) {
		this.count = count;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float px = (float) Math.atan2(x, z);
		float pz = MathUtil.length(x, z);
		return this.source.getDistance(px, y, pz);
	}
}
