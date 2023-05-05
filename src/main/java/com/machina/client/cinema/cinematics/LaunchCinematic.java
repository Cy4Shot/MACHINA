package com.machina.client.cinema.cinematics;

import com.machina.block.ShipConsoleBlock;
import com.machina.client.cinema.CameraPath;
import com.machina.client.cinema.CameraPath.InterpolationMethod;
import com.machina.client.cinema.effect.ActionEffect;
import com.machina.client.cinema.effect.CameraEffect;
import com.machina.client.cinema.effect.OverlayEffect;
import com.machina.client.cinema.effect.ParticleEffect;
import com.machina.client.cinema.effect.ShakeEffect;
import com.machina.network.MachinaNetwork;
import com.machina.network.c2s.C2SShipLaunchEffect;
import com.machina.network.c2s.C2SSpawnParticle;
import com.machina.util.MachinaRL;
import com.machina.util.helper.DirectionHelper;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class LaunchCinematic extends PathCinematic {

	BlockPos pos;

	public LaunchCinematic(ClientPlayerEntity p, BlockPos pos) {
		super(p);
		this.pos = pos;

		Direction d = p.level.getBlockState(pos).getValue(ShipConsoleBlock.FACING);
		int yaw = DirectionHelper.toYaw(d.getOpposite());

		Vector3d o = p.position();

		CameraEffect SHAKE1 = new ShakeEffect(0.3f);
		CameraEffect SHAKE2 = new ShakeEffect(0.75f);
		CameraEffect SMOKE = new ParticleEffect(ParticleTypes.CAMPFIRE_COSY_SMOKE, p.position().add(0, -2.1D, 0), 0.05D,
				0f, 0.8f);
		CameraEffect EXPLOSIONS = new ParticleEffect(ParticleTypes.EXPLOSION, p.position().add(0, -2.1D, 0), 0.05D, 1f,
				0.1f);
		CameraEffect LAUNCH = new ActionEffect(ting -> {
			double off = Math.pow(Math.E, (double) ting / 9D) - 1D;
			clientEntity.moveTo(o.add(0, off, 0));
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(ParticleTypes.FLAME, -0.1f, 20,
					o.add(0, off - 2.1D, 0), new Vector3d(0d, 1d, 0d)));
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(ParticleTypes.ANGRY_VILLAGER, -0.1f, 2,
					o.add(0, off - 2.1D, 0), new Vector3d(1d, 1d, 1d)));
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, -0.1f, 5,
					o.add(0, off - 2.1D, 0), new Vector3d(0.1d, 1d, 0.1d)));
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, -0.1f, 10,
					o.add(0, off - 2.1D, 0), new Vector3d(0d, 1d, 0d)));
			MachinaNetwork.CHANNEL.sendToServer(new C2SSpawnParticle(ParticleTypes.EXPLOSION, -0.1f, 6,
					o.add(0, off - 2.1D, 0), new Vector3d(0d, 1d, 0d)));
			MachinaNetwork.CHANNEL.sendToServer(new C2SShipLaunchEffect(pos, ting));
		});

		CameraEffect OVERLAY = new ActionEffect(ting -> {
			if (ting == 1) {
				OverlayEffect.rl = new MachinaRL("textures/gui/machina.png");
				OverlayEffect.render = true;
			} else if (ting == 179) {
				OverlayEffect.render = false;
			}
			OverlayEffect.opacity = Math.max(0f, (float) (ting - 100) / 80f);
		});

		// @formatter:off
		setPath(CameraPath.builder(p.position())
		.addPath(InterpolationMethod.LERP, 150, effects(SHAKE1),
				node(0, 0, 0, p.xRot, yaw),
				node(d.getStepX() * 10, 0, d.getStepZ() * 10, p.xRot, yaw))
		.addPath(InterpolationMethod.LERP, 200, effects(SHAKE1, SMOKE, EXPLOSIONS),
				node(d.getStepX() * 10, 0, d.getStepZ() * 10, p.xRot, yaw),
				node(d.getStepX() * 10, 1, d.getStepZ() * 10, p.xRot, yaw))
		.addPath(InterpolationMethod.BEZIER, 180, effects(SHAKE2, LAUNCH, OVERLAY),
				node(d.getStepX() * 10, 1, d.getStepZ() * 10, p.xRot, yaw),
				node(d.getStepX() * 10, 1, d.getStepZ() * 10, -80, yaw),
				node(d.getStepX() * 10, 1, d.getStepZ() * 10, -85, yaw),
				node(d.getStepX() * 10, 1, d.getStepZ() * 10, -90, yaw))
		.build());
		// @formatter:on
	}

	@Override
	public void finish(float partial) {
		super.finish(partial);
		MachinaNetwork.CHANNEL.sendToServer(new C2SShipLaunchEffect(pos, 0));
	}

}
