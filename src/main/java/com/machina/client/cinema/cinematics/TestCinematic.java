package com.machina.client.cinema.cinematics;

import com.machina.client.cinema.CameraPath;
import com.machina.client.cinema.Cinematic;
import com.machina.client.cinema.CameraPath.InterpolationMethod;
import com.machina.client.cinema.entity.CameraClientEntity;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public class TestCinematic extends Cinematic {

	CameraPath path;

	public TestCinematic(ClientPlayerEntity p) {
		super(new CameraClientEntity());

		path = CameraPath.builder(p.position()).addPoint(0, 0, 0, p.xRot, p.yRot).addPoint(0, 10, 0, p.xRot, p.yRot)
				.addPoint(0, 0, 0, p.xRot, p.yRot).build(InterpolationMethod.LERP);
	}

	@Override
	public void onClientTick(float per, float par) {
		if (!this.active)
			return;
		path.lerp(this.player, per, par);
	}

	@Override
	public int getDuration() {
		return 100;
	}
}
