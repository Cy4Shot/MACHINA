package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

import net.minecraft.util.Mth;

public class SDFSmoothUnion extends SDFBinary {

	private final float radius;

	public SDFSmoothUnion(SDF sourceA, SDF sourceB, float radius) {
		super(sourceA, sourceB);
		this.radius = radius;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float a = this.sourceA.getDistance(x, y, z);
		float b = this.sourceB.getDistance(x, y, z);
		this.selectValue(a, b);
		float h = Mth.clamp(0.5F + 0.5F * (b - a) / radius, 0F, 1F);
		return Mth.lerp(h, b, a) - radius * h * (1F - h);
	}
}
