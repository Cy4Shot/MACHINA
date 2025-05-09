package com.machina.api.rocket;

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

	public RocketPart(RocketPartType type, Vector3d upAnchor, Vector3d downAnchor, Supplier<T> model) {
		this.type = type;
		this.upAnchor = upAnchor;
		this.downAnchor = downAnchor;
		this.model = model;
	}

	@OnlyIn(Dist.CLIENT)
	public T bake() {
		return model.get();
	}
}