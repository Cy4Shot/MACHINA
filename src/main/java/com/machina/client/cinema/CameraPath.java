package com.machina.client.cinema;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.machina.client.cinema.effect.CameraEffect;
import com.machina.util.math.MathUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class CameraPath {

	private LinkedHashMap<SinglePath, Integer> paths;
	private Vector3d origin;

	public CameraPath(LinkedHashMap<SinglePath, Integer> ps, Vector3d o) {
		this.paths = ps;
		this.origin = o;
	}

	public void tick(PlayerEntity renderView, int tick, float partial) {
		int elapsed = 0;
		for (Map.Entry<SinglePath, Integer> path : paths.entrySet()) {
			if (elapsed + path.getValue() >= tick) {
				float per = (float) (tick - elapsed) / (float) path.getValue();
				path.getKey().interpolate(renderView, per, partial, origin);
				return;
			} else {
				elapsed += path.getValue();
			}
		}
	}

	public void renderTick(PlayerEntity renderView, int tick, float partial) {
		int elapsed = 0;
		for (Map.Entry<SinglePath, Integer> path : paths.entrySet()) {
			if (elapsed + path.getValue() >= tick) {
				for (CameraEffect e : path.getKey().effects)
					e.tickEffect(tick - elapsed);
				return;
			} else {
				elapsed += path.getValue();
			}
		}
	}

	public int duration() {
		return paths.values().stream().mapToInt(Integer::intValue).sum();
	}

	public static Builder builder(Vector3d origin) {
		return new Builder(origin);
	}

	public static class Builder {
		private Vector3d o;
		private LinkedHashMap<SinglePath, Integer> p = new LinkedHashMap<SinglePath, Integer>();

		public Builder(Vector3d origin) {
			this.o = origin;
		}

		public Builder addPath(InterpolationMethod method, int duration, List<CameraEffect> effects,
				CameraNode... points) {
			p.put(new SinglePath(method, o, effects, points), duration);
			return this;
		}

		public CameraPath build() {
			return new CameraPath(p, o);
		}
	}

	public static class SinglePath {
		private InterpolationMethod method;
		private List<CameraNode> points;
		private List<CameraEffect> effects;

		public SinglePath(InterpolationMethod method, Vector3d origin, List<CameraEffect> effects,
				CameraNode... nodes) {
			this.method = method;
			this.effects = effects;
			this.points = Arrays.asList(nodes);
		}

		public void interpolate(PlayerEntity render, float per, float par, Vector3d o) {

			Vector3d pos = render.position();
			float pitch = render.xRot;
			float yaw = render.yRot;

			if (method == InterpolationMethod.BEZIER) {
				pos = MathUtil.bezier(per, points.stream().map(p -> p.pos).toArray(Vector3d[]::new));
				pitch = MathUtil.bezier(per, points.stream().map(p -> p.xRot).toArray(Float[]::new));
				yaw = MathUtil.bezier(per, points.stream().map(p -> p.yRot).toArray(Float[]::new));
			} else if (method == InterpolationMethod.LERP) {
				pos = MathUtil.lerp(points.get(0).pos, points.get(1).pos, per);
				pitch = MathUtil.lerp(points.get(0).xRot, points.get(1).xRot, per);
				yaw = MathUtil.lerp(points.get(0).yRot, points.get(1).yRot, per);
			}

			pos = pos.add(o);

			CameraUtil.positionCamera(render, par, pos.x, pos.y, pos.z, render.getX(), render.getY(), render.getZ(),
					yaw, render.yRot, pitch, render.xRot);
		}
	}

	public static class CameraNode {
		public Vector3d pos;
		public float xRot, yRot;

		public CameraNode(Vector3d p, float x, float y) {
			this.pos = p;
			this.xRot = x;
			this.yRot = y;
		}
	}

	public enum InterpolationMethod {
		BEZIER, LERP
	}
}
