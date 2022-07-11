package com.machina.client.cinema;

import java.util.ArrayList;
import java.util.List;

import com.machina.util.math.MathUtil;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class CameraPath {

	private List<CameraNode> points;
	private InterpolationMethod method;

	public CameraPath(List<CameraNode> ps, InterpolationMethod me) {
		this.points = ps;
		this.method = me;
	}

	public void lerp(PlayerEntity renderView, float per, float partial) {
		// Get Nodes
		int point = (int) Math.floor(per * (points.size() - 1));
		CameraNode one = points.get(point);
		CameraNode two = points.get(point + 1);

		// Lerp
		Vector3d pos = MathUtil.lerp(one.pos, two.pos, per);
		float pitch = MathUtil.lerp(one.xRot, two.xRot, per);
		float yaw = MathUtil.lerp(one.yRot, two.yRot, per);

		// Apply
		CameraUtil.positionCamera(renderView, partial, pos.x, pos.y, pos.z, renderView.getX(), renderView.getY(),
				renderView.getZ(), yaw, renderView.yRot, pitch, renderView.xRot);
	}

	public static Builder builder(Vector3d origin) {
		return new Builder(origin);
	}

	public static class Builder {
		private Vector3d o;
		private List<CameraNode> p = new ArrayList<CameraNode>();

		public Builder(Vector3d origin) {
			this.o = origin;
		}

		public Builder addPoint(double x, double y, double z, float pitch, float yaw) {
			p.add(new CameraNode(new Vector3d(o.x + x, o.y + y, o.z + z), pitch, yaw));
			return this;
		}

		public CameraPath build(InterpolationMethod method) {
			return new CameraPath(p, method);
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
