package com.machina.client.cinema;

import java.util.Arrays;
import java.util.List;

import com.machina.api.client.cinema.CameraPath;
import com.machina.api.client.cinema.CameraPath.CameraNode;
import com.machina.api.client.cinema.Cinematic;
import com.machina.api.client.cinema.effect.CameraEffect;
import com.machina.api.client.cinema.entity.CameraClientEntity;

import net.minecraft.world.phys.Vec3;

public class PathCinematic extends Cinematic {

	CameraPath path;

	public PathCinematic(String id, CameraClientEntity p) {
		super(id, p);
	}

	public void setPath(CameraPath path) {
		this.path = path;
	}

	@Override
	public void onClientTick(int tick, float par) {
		if (!this.active)
			return;
		path.applyEffects(tick);
	}

	@Override
	public int getDuration() {
		return path.duration();
	}

	public static CameraNode node(double x, double y, double z, float pitch, float yaw) {
		return new CameraNode(new Vec3(x, y, z), pitch, yaw);
	}

	@Override
	public void onRenderTick(int tick, float par) {
		if (!this.active)
			return;
		path.renderTick(this.player, tick, par);
	}

	public static List<CameraEffect> effects(CameraEffect... eff) {
		return Arrays.asList(eff);
	}
}