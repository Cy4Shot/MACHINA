package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;

public class SDFCopyRotate extends SDFUnary {

	final int count;

	public SDFCopyRotate(SDF source, int count) {
		super(source);
		this.count = count;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float px = (float) Math.atan2(x, z);
		float pz = MathUtil.length(x, z);
		return this.source.getDistance(px, y, pz);
	}
}
