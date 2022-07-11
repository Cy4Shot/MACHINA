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

	public void lerp(PlayerEntity renderView, float per, float partial) {
		// Get Nodes
		int point = (int) Math.floor(per * (points.size() - 1));
		CameraNode one = points.get(point);
		CameraNode two = points.get(point + 1);
		
		// Lerp
		Vector3d pos = two.pos.scale(per).add(one.pos.scale(1 - per));
		float pitch = two.xRot * per + one.xRot * (1 - per);
		float yaw = two.yRot * per + one.yRot * (1 - per);

		// Apply
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
