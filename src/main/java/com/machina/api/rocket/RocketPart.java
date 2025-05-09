package com.machina.api.rocket;

import org.joml.Vector3d;

public class RocketPart {
	private final RocketPartType type;
	private final Vector3d upAnchor;
	private final Vector3d downAnchor;

	public RocketPart(RocketPartType type, Vector3d upAnchor, Vector3d downAnchor) {
		this.type = type;
		this.upAnchor = upAnchor;
		this.downAnchor = downAnchor;
	}
}