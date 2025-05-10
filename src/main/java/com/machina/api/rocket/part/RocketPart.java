package com.machina.api.rocket.part;

import java.util.function.Supplier;

import org.joml.Vector3d;

import com.machina.client.rocket.model.RocketPartModel;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RocketPart<T extends RocketPartModel> {
	private final RocketPartType type;
	private final Vector3d upAnchor;
	private final Vector3d downAnchor;
	private final Supplier<T> model;

	private final float weight;

	public RocketPart(RocketPartType type, Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model, float weight) {
		this.type = type;
		this.upAnchor = upAnchor;
		this.downAnchor = downAnchor;
		this.model = model;
		this.weight = weight;
	}

	@OnlyIn(Dist.CLIENT)
	public T bake() {
		return model.get();
	}

	public float getWeight() {
		return weight;
	}

	public Vector3d getDownAnchor() {
		return downAnchor;
	}

	public Vector3d getUpAnchor() {
		return upAnchor;
	}

	public RocketPartType getType() {
		return type;
	}
}