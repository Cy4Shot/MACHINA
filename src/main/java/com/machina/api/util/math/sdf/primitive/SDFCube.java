package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;

public class SDFCube extends SDFPrimitive {
	private final float halfSize;

	public SDFCube(float size) {
		this.halfSize = size * 0.5f;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float dx = Math.abs(x) - halfSize;
		float dy = Math.abs(y) - halfSize;
		float dz = Math.abs(z) - halfSize;

		float ox = Math.max(dx, 0f);
		float oy = Math.max(dy, 0f);
		float oz = Math.max(dz, 0f);

		float outsideDistance = MathUtil.length(ox, oy, oz);
		float insideDistance = Math.min(Math.max(dx, Math.max(dy, dz)), 0f);

		return outsideDistance + insideDistance;
	}
}
