package com.machina.client.cinema.cinematics;

import com.machina.client.cinema.CameraPath;
import com.machina.client.cinema.Cinematic;
import com.machina.client.cinema.entity.CameraClientEntity;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public class TestCinematic extends Cinematic {

	CameraPath path;

	public TestCinematic(ClientPlayerEntity p) {
		super(new CameraClientEntity());

		path = CameraPath.builder().addPoint(p.getX(), p.getY(), p.getZ(), p.xRot, p.yRot)
				.addPoint(p.getX(), p.getY() + 10, p.getZ(), p.xRot, p.yRot)
				.addPoint(p.getX(), p.getY(), p.getZ(), p.xRot, p.yRot).build();
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
