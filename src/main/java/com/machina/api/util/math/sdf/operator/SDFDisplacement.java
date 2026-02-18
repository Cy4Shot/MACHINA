package com.machina.api.util.math.sdf.operator;

import java.util.function.Function;

import org.joml.Vector3f;

import com.machina.api.util.math.OpenSimplex2F;
import com.machina.api.util.math.sdf.SDF;

import net.minecraft.util.RandomSource;

public class SDFDisplacement extends SDFUnary {

	private final Vector3f pos = new Vector3f();
	private final Function<Vector3f, Float> displace;

	public SDFDisplacement(SDF source, RandomSource random, float max) {
		this(source, x -> random.nextFloat() * max - max / 2);
	}

	public SDFDisplacement(SDF source, OpenSimplex2F noise, float inner, float outer) {
		this(source, v -> (float) noise.noise3_Classic(v.x() * inner, v.y() * inner, v.z() * inner) * outer);
	}

	public SDFDisplacement(SDF source, Function<Vector3f, Float> displace) {
		super(source);
		this.displace = displace;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		pos.set(x, y, z);
		return this.source.getDistance(x, y, z) + displace.apply(pos);
	}
}
