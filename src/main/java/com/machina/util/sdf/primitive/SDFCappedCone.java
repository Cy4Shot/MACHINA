package com.machina.util.sdf.primitive;

import com.machina.util.math.MathUtil;

import net.minecraft.util.math.MathHelper;

public class SDFCappedCone extends SDFPrimitive {
	private float radius1;
	private float radius2;
	private float height;

	public SDFCappedCone setRadius1(float radius) {
		this.radius1 = radius;
		return this;
	}

	public SDFCappedCone setRadius2(float radius) {
		this.radius2 = radius;
		return this;
	}

	public SDFCappedCone setHeight(float height) {
		this.height = height;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float qx = MathUtil.length(x, z);
		float k2x = radius2 - radius1;
		float k2y = 2 * height;
		float cax = qx - MathUtil.min(qx, (y < 0F) ? radius1 : radius2);
		float cay = Math.abs(y) - height;
		float mlt = MathHelper
				.clamp(MathUtil.dot(radius2 - qx, height - y, k2x, k2y) / MathUtil.dot(k2x, k2y, k2x, k2y), 0F, 1F);
		float cbx = qx - radius2 + k2x * mlt;
		float cby = y - height + k2y * mlt;
		float s = (cbx < 0F && cay < 0F) ? -1F : 1F;
		return s * (float) Math.sqrt(MathUtil.min(MathUtil.dot(cax, cay, cax, cay), MathUtil.dot(cbx, cby, cbx, cby)));
	}
}
