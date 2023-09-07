package com.machina.api.client.cinema.effect;

import com.machina.api.util.math.MathUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public record ShakeEffect(float intensity) implements CameraEffect {

	private static final Minecraft mc = Minecraft.getInstance();

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