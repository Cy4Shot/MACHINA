package com.machina.client.cinema.cinematics;

import com.machina.client.cinema.CameraPath;
import com.machina.client.cinema.CameraPath.InterpolationMethod;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public class TestCinematic extends PathCinematic {

	public TestCinematic(ClientPlayerEntity p) {
		// @formatter:off
		super(p, CameraPath.builder(p.position())
				.addPath(InterpolationMethod.LERP, 100,
						node(0, 0, 0, p.xRot, p.yRot),
						node(0, 10, 0, p.xRot, p.yRot))
				.addPath(InterpolationMethod.BEZIER, 150,
						node(0, 10, 0, p.xRot, p.yRot),
						node(-5, 7, 0, p.xRot, p.yRot),
						node(-5, 3, 0, p.xRot, p.yRot),
						node(0, 0, 0, p.xRot, p.yRot))
				.build());
		// @formatter:on
	}
}
