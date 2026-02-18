package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;
import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.util.Mth;

public class SDFHeightmap extends SDFDisplacement {

	public SDFHeightmap(SDF source, NativeImage map, float angle, float scale, float intensity) {
		super(source, pos -> {
			if (map == null) {
				return 0F;
			}
			float sin = Mth.sin(angle);
			float cos = Mth.cos(angle);
			float s = map.getWidth() * scale;
			float offsetX = map.getWidth() * 0.5F;
			float offsetZ = map.getHeight() * 0.5F;
			float px = Mth.clamp(pos.x() * s + offsetX, 0, map.getWidth() - 2);
			float pz = Mth.clamp(pos.z() * s + offsetZ, 0, map.getHeight() - 2);
			float dx = (px * cos - pz * sin);
			float dz = (pz * cos + px * sin);
			int x1 = Mth.floor(dx);
			int z1 = Mth.floor(dz);
			int x2 = x1 + 1;
			int z2 = z1 + 1;
			dx = dx - x1;
			dz = dz - z1;
			float a = (map.getPixelRGBA(x1, z1) & 255) / 255F;
			float b = (map.getPixelRGBA(x2, z1) & 255) / 255F;
			float c = (map.getPixelRGBA(x1, z2) & 255) / 255F;
			float d = (map.getPixelRGBA(x2, z2) & 255) / 255F;
			a = Mth.lerp(dx, a, b);
			b = Mth.lerp(dx, c, d);
			return -Mth.lerp(dz, a, b) * intensity;
		});
	}
}
