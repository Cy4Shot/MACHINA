package com.machina.util.sdf.operator;

import java.util.function.Consumer;

import net.minecraft.util.math.vector.Vector3f;

public class SDFCoordModify extends SDFUnary {
	private final Vector3f pos = new Vector3f();
	private Consumer<Vector3f> function;

	public SDFCoordModify setFunction(Consumer<Vector3f> function) {
		this.function = function;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		pos.set(x, y, z);
		function.accept(pos);
		return this.source.getDistance(pos.x(), pos.y(), pos.z());
	}
}
