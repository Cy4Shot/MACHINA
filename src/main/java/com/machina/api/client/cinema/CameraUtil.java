package com.machina.api.client.cinema;

import com.machina.api.util.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CameraUtil {
	protected static final Minecraft mc = Minecraft.getInstance();

	public static void resetCamera() {
		if (mc.player != null) {
			mc.setCameraEntity(mc.player);
			if (mc.screen != null)
				mc.setScreen(null);
		}
	}

	public static void positionCamera(Player renderView, float pTicks, double x, double y, double z, double prevX,
			double prevY, double prevZ, double yaw, double yawPrev, double pitch, double pitchPrev) {
		double dYaw = MathUtil.positiveModulo(yaw - yawPrev, 360d);
		// Use the smaller arc
		if (dYaw > 180) {
			dYaw -= 360;
		}
		yawPrev = yaw - dYaw;
		float iYaw = MathUtil.lerp((float) yawPrev, (float) yaw, pTicks);
		float iPitch = MathUtil.lerp((float) pitchPrev, (float) pitch, pTicks);

		Entity rv = mc.getCameraEntity();
		if (rv == null || !rv.equals(renderView)) {
			mc.setCameraEntity(renderView);
			rv = renderView;
		}

		Player render = (Player) rv;
		render.setPosRaw(x, y, z);
		render.xo = prevX;
		render.yo = prevY;
		render.zo = prevZ;
		render.xOld = prevX;
		render.yOld = prevY;
		render.zOld = prevZ;
		render.setYRot(iYaw);
		render.yRotO = iYaw;
		render.yHeadRot = iYaw;
		render.yHeadRotO = iYaw;
		render.bob = iYaw;
		render.oBob = iYaw;
		render.yBodyRot = iYaw;
		render.yBodyRotO = iYaw;
		render.setXRot(iPitch);
		render.xRotO = iPitch;
		render = mc.player;
		render.setPosRaw(x, y, z);
		render.xo = prevX;
		render.yo = prevY;
		render.zo = prevZ;
		render.xOld = prevX;
		render.yOld = prevY;
		render.zOld = prevZ;
		render.setYRot(iYaw);
		render.yRotO = iYaw;
		render.yHeadRot = iYaw;
		render.yHeadRotO = iYaw;
		render.bob = iYaw;
		render.oBob = iYaw;
		render.yBodyRot = iYaw;
		render.yBodyRotO = iYaw;
		render.setXRot(iPitch);
		render.xRotO = iPitch;
	}
}
