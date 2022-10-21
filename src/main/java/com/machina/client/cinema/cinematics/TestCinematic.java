package com.machina.client.cinema.cinematics;

import com.machina.client.cinema.CameraPath;
import com.machina.client.cinema.CameraPath.InterpolationMethod;
import com.machina.client.cinema.effect.ActionEffect;
import com.machina.client.cinema.effect.CameraEffect;
import com.machina.client.cinema.effect.OverlayVideoEffect;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SShipLaunchEffect;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TestCinematic extends PathCinematic {

	BlockPos pos;

	public TestCinematic(ClientPlayerEntity p, BlockPos pos) {
		super(p);
		this.pos = pos;

		CameraEffect OVERLAY2 = new ActionEffect(ting -> {
			if (ting == 1) {
				OverlayVideoEffect.setup(null, 1f);
				OverlayVideoEffect.render = true;
			} else if (ting == 1800) {
				OverlayVideoEffect.render = false;
			}
		});

		// @formatter:off
		setPath(CameraPath.builder(p.position())
		.addPath(InterpolationMethod.LERP, 1800, effects(OVERLAY2),
				node(0, 0, 0, p.xRot, p.yRot),
				node(1 * 10, 0, 1 * 10, p.xRot, p.yRot))
		.build());
		// @formatter:on
	}

	@Override
	public void finish(float partial) {
		super.finish(partial);
		MachinaNetwork.CHANNEL.sendToServer(new C2SShipLaunchEffect(pos, 0));
	}

}