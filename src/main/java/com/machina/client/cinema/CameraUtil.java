package com.machina.client.cinema;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class CameraUtil {
	
	protected static final Minecraft mc = Minecraft.getInstance();

	public static void resetCamera() {
		if (mc.player != null) {
			mc.setCameraEntity(mc.player);
			if (mc.screen != null)
				mc.setScreen(null);
		}
	}
	
	public static void positionCamera(PlayerEntity renderView, float pTicks, double x, double y, double z, double prevX,
			double prevY, double prevZ, double yaw, double yawPrev, double pitch, double pitchPrev) {
		double dYaw = MathHelper.positiveModulo(yaw - yawPrev, 360d);
		// Use the smaller arc
		if (dYaw > 180) {
			dYaw -= 360;
		}
		yawPrev = yaw - dYaw;
		float iYaw = MathHelper.lerp(pTicks, (float) yawPrev, (float) yaw);
		float iPitch = MathHelper.lerp(pTicks, (float) pitchPrev, (float) pitch);

		Entity rv = mc.getCameraEntity();
		if (rv == null || !rv.equals(renderView)) {
			mc.setCameraEntity(renderView);
			rv = renderView;
		}

		PlayerEntity render = (PlayerEntity) rv;
		render.setPosRaw(x, y, z);
		render.xo = prevX;
		render.yo = prevY;
		render.zo = prevZ;
		render.xOld = prevX;
		render.yOld = prevY;
		render.zOld = prevZ;
		render.yRot = iYaw;
		render.yRotO = iYaw;
		render.yHeadRot = iYaw;
		render.yHeadRotO = iYaw;
		render.bob = iYaw;
		render.oBob = iYaw;
		render.yBodyRot = iYaw;
		render.yBodyRotO = iYaw;
		render.xRot = iPitch;
		render.xRotO = iPitch;
		render = mc.player;
		render.setPosRaw(x, y, z);
		render.xo = prevX;
		render.yo = prevY;
		render.zo = prevZ;
		render.xOld = prevX;
		render.yOld = prevY;
		render.zOld = prevZ;
		render.yRot = iYaw;
		render.yRotO = iYaw;
		render.yHeadRot = iYaw;
		render.yHeadRotO = iYaw;
		render.bob = iYaw;
		render.oBob = iYaw;
		render.yBodyRot = iYaw;
		render.yBodyRotO = iYaw;
		render.xRot = iPitch;
		render.xRotO = iPitch;
	}
}
