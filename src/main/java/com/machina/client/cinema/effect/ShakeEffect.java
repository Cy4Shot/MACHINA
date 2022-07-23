package com.machina.client.cinema.effect;

import com.machina.util.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ShakeEffect extends CameraEffect {

	private static final Minecraft mc = Minecraft.getInstance();
	private float intensity;

	public ShakeEffect(float i) {
		intensity = i;
	}

	@Override
	public void tickEffect(int tick) {
		if (!mc.isPaused() && mc.screen == null) {
			Entity camera = mc.getCameraEntity();
			if (camera != null) {
				camera.xRot = camera.xRot + (float) Math.sin(Math.random() * MathUtil.TWO_PI) * intensity;
				camera.yRot = camera.yRot + (float) Math.sin(Math.random() * MathUtil.TWO_PI) * intensity;
			}
		}
	}
}
