package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.OpenSimplex2F;
import com.machina.api.util.math.sdf.SDF;

import net.minecraft.util.Mth;

public class SDFRadialNoiseMap extends SDFDisplacement {
	private static final float SIN = MathUtil.sin(0.5F);
	private static final float COS = MathUtil.cos(0.5F);

	public SDFRadialNoiseMap(SDF source, long seed, float intensity, float radius, int x, int y) {
		super(source, pos -> {
			if (intensity == 0) {
				return 0F;
			}
			float px = pos.x() / radius;
			float pz = pos.z() / radius;
			float distance = px * px + pz * pz;
			if (distance > 1) {
				return 0F;
			}
			float offsetX = (short) (x & 32767);
			float offsetZ = (short) (y & 32767);
			OpenSimplex2F noise = new OpenSimplex2F(seed);
			distance = 1 - Mth.sqrt(distance);
			float nx = px * COS - pz * SIN;
			float nz = pz * COS + px * SIN;
			distance *= getNoise(noise, nx * 0.75 + offsetX, nz * 0.75 + offsetZ);
			return distance * intensity;
		});
	}

	private static float getNoise(OpenSimplex2F noise, double x, double z) {
		return (float) noise.noise2(x, z) + (float) noise.noise2(x * 3 + 1000, z * 3) * 0.5F
				+ (float) noise.noise2(x * 9 + 1000, z * 9) * 0.2F;
	}
}
