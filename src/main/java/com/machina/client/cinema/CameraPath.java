package com.machina.client.cinema;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class CameraPath {

	private List<CameraNode> points;

	public CameraPath(List<CameraNode> ps) {
		this.points = ps;
	}

	public void lerp(PlayerEntity renderView, float percentage, float partial) {
		int size = points.size() - 1;
		int point = (int) Math.floor(percentage * size);
		CameraNode one = points.get(point);
		CameraNode two = points.get(point + 1);
		float dist = (percentage - point * size) / (1f / size);
		Vector3d pos = one.pos.scale(dist).add(two.pos.scale(1 - dist));
		float pitch = one.xRot * dist + two.xRot * (1 - dist);
		float yaw = one.yRot * dist + two.yRot * (1 - dist);

		CameraUtil.positionCamera(renderView, partial, pos.x, pos.y, pos.z, renderView.getX(), renderView.getY(),
				renderView.getZ(), yaw, renderView.yRot, pitch, renderView.xRot);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private List<CameraNode> p = new ArrayList<CameraNode>();

		public Builder addPoint(double x, double y, double z, float pitch, float yaw) {
			p.add(new CameraNode(new Vector3d(x, y, z), pitch, yaw));
			return this;
		}

		public CameraPath build() {
			return new CameraPath(p);
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

}
