package com.machina.client.explosion;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosionEffects {

	private static final Minecraft mc = Minecraft.getInstance();

	// Track shake state
	private static boolean wasShaking = false;
	private static long shakeStartTime = 0;
	private static float lastXOffset = 0f;
	private static float lastYOffset = 0f;

	public static void tick() {
		if (mc.player == null || mc.level == null || mc.isPaused()) {
			return;
		}

		float maxShake = 0f;

		Vec3 playerPos = mc.player.position();

		for (NuclearExplosion explosion : NuclearExplosionManager.getActiveExplosions()) {
			if (!explosion.getDimension().equals(mc.level.dimension())) {
				continue;
			}

			float progress = explosion.getProgress();
			float power = explosion.getPower();
			Vec3 explosionPos = explosion.getPosition();

			double distance = playerPos.distanceTo(explosionPos);
			float maxRange = power * 300.0f;

			if (distance < maxRange) {
				// Gentler distance scaling - sqrt instead of square
				float distanceFactor = 1.0f - (float) (distance / maxRange);
				distanceFactor = (float) Math.sqrt(distanceFactor);

				// Camera shake - intense at start, fades out
				float shakeIntensity = getShakeIntensity(progress) * distanceFactor;
				maxShake = Math.max(maxShake, shakeIntensity);
			}
		}

		applyShake(maxShake);
	}

	private static float getShakeIntensity(float progress) {
		// Shake fades over first 15% - less dependent on power
		if (progress < 0.15f) {
			float shakeFade = 1.0f - (progress / 0.15f);
			return 3.0f * shakeFade * shakeFade;  // Fixed intensity, not scaled by power
		}
		return 0f;
	}

	private static void applyShake(float intensity) {
		Entity camera = mc.getCameraEntity();
		if (camera == null) return;

		if (intensity > 0.01f && mc.screen == null) {
			if (!wasShaking) {
				shakeStartTime = System.currentTimeMillis();
				wasShaking = true;
				lastXOffset = 0f;
				lastYOffset = 0f;
			}

			// Time-based oscillation (not random walk)
			float time = (System.currentTimeMillis() - shakeStartTime) / 1000.0f;
			float freq1 = 23.7f;
			float freq2 = 31.3f;

			// Calculate new offset
			float xOffset = (float) (Math.sin(time * freq1) * 0.7 + Math.sin(time * freq2 * 1.3) * 0.3) * intensity;
			float yOffset = (float) (Math.sin(time * freq2) * 0.7 + Math.cos(time * freq1 * 0.9) * 0.3) * intensity;

			// Apply delta from last offset (so mouse movement isn't lost)
			camera.setXRot(camera.getXRot() - lastXOffset + xOffset);
			camera.setYRot(camera.getYRot() - lastYOffset + yOffset);

			lastXOffset = xOffset;
			lastYOffset = yOffset;
		} else if (wasShaking) {
			// Remove last offset when stopping
			camera.setXRot(camera.getXRot() - lastXOffset);
			camera.setYRot(camera.getYRot() - lastYOffset);
			wasShaking = false;
			lastXOffset = 0f;
			lastYOffset = 0f;
		}
	}
}
