package com.machina.api.util.math.sdf.primitive;

import com.machina.api.util.math.MathUtil;

import net.minecraft.util.Mth;

public class SDFCappedCone extends SDFPrimitive {
	private final float radius1;
	private final float radius2;
	private final float height;

	public SDFCappedCone(float r1, float r2, float height) {
		this.radius1 = r1;
		this.radius2 = r2;
		this.height = height;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float qx = MathUtil.length(x, z);
		float k2x = radius2 - radius1;
		float k2y = 2 * height;
		float cax = qx - MathUtil.min(qx, (y < 0F) ? radius1 : radius2);
		float cay = Math.abs(y) - height;
		float mlt = Mth.clamp(MathUtil.dot(radius2 - qx, height - y, k2x, k2y) / MathUtil.dot(k2x, k2y, k2x, k2y), 0F,
				1F);
		float cbx = qx - radius2 + k2x * mlt;
		float cby = y - height + k2y * mlt;
		float s = (cbx < 0F && cay < 0F) ? -1F : 1F;
		return s * (float) Math.sqrt(MathUtil.min(MathUtil.dot(cax, cay, cax, cay), MathUtil.dot(cbx, cby, cbx, cby)));
	}
}
