package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

public class SDFFlatWave extends SDFDisplacement {

	public SDFFlatWave(SDF source, int count, float angle, float intensity) {
		super(source, pos -> (float) Math.cos(Math.atan2(pos.x(), pos.z()) * count + angle) * intensity);
	}
}
