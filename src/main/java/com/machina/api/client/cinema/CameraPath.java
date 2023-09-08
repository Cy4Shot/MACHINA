package com.machina.api.client.cinema;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.machina.api.client.cinema.effect.CameraEffect;
import com.machina.api.util.math.MathUtil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CameraPath {

	private LinkedHashMap<SinglePath, Integer> paths;
	private Vec3 origin;

	public CameraPath(LinkedHashMap<SinglePath, Integer> ps, Vec3 o) {
		this.paths = ps;
		this.origin = o;
	}

	public void tick(Player renderView, int tick, float partial) {
		int elapsed = 0;
		for (Map.Entry<SinglePath, Integer> path : paths.entrySet()) {
			if (elapsed + path.getValue() >= tick + partial) {
				float per = (float) (tick + partial - elapsed) / (float) path.getValue();
				path.getKey().interpolate(renderView, per, partial, origin);
				return;
			} else {
				elapsed += path.getValue();
			}
		}
	}

	public void renderTick(Player renderView, int tick, float partial) {
		this.tick(renderView, tick, partial);
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

	public static Builder builder() {
		return new Builder(Vec3.ZERO);
	}

	public static Builder builder(Vec3 origin) {
		return new Builder(origin);
	}

	public static class Builder {
		private Vec3 o;
		private LinkedHashMap<SinglePath, Integer> p = new LinkedHashMap<SinglePath, Integer>();

		public Builder(Vec3 origin) {
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

		public SinglePath(InterpolationMethod method, Vec3 origin, List<CameraEffect> effects, CameraNode... nodes) {
			this.method = method;
			this.effects = effects;
			this.points = Arrays.asList(nodes);
		}

		public void interpolate(Player render, float per, float par, Vec3 o) {

			Vec3 pos = render.position();
			float pitch = render.getXRot();
			float yaw = render.getYRot();

			if (method == InterpolationMethod.BEZIER) {
				pos = MathUtil.bezier(per, points.stream().map(p -> p.pos).toArray(Vec3[]::new));
				pitch = MathUtil.bezier(per, points.stream().map(p -> p.xRot).toArray(Float[]::new));
				yaw = MathUtil.bezier(per, points.stream().map(p -> p.yRot).toArray(Float[]::new));
			} else if (method == InterpolationMethod.LERP) {
				pos = MathUtil.lerp(points.get(0).pos, points.get(1).pos, per);
				pitch = MathUtil.lerp(points.get(0).xRot, points.get(1).xRot, per);
				yaw = MathUtil.lerp(points.get(0).yRot, points.get(1).yRot, per);
			}

			pos = pos.add(o);

			CameraUtil.positionCamera(render, par, pos.x, pos.y, pos.z, render.getX(), render.getY(), render.getZ(),
					yaw, render.getYRot(), pitch, render.getXRot());
		}
	}

	public static class CameraNode {
		public Vec3 pos;
		public float xRot, yRot;

		public CameraNode(Vec3 p, float x, float y) {
			this.pos = p;
			this.xRot = x;
			this.yRot = y;
		}
	}

	public enum InterpolationMethod {
		BEZIER, LERP
	}
}