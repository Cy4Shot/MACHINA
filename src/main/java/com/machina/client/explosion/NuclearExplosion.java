package com.machina.client.explosion;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NuclearExplosion {

	private final Vec3 position;
	private final long startTime;
	private final float power;
	private final int durationTicks;
	private final ResourceKey<Level> dimension;
	private final RandomSource random;

	public NuclearExplosion(BlockPos pos, float power, ResourceKey<Level> dimension) {
		this.position = Vec3.atCenterOf(pos);
		this.startTime = System.currentTimeMillis();
		this.power = power;
		this.durationTicks = (int) (power * 800);
		this.dimension = dimension;
		this.random = RandomSource.create();
	}

	public NuclearExplosion(Vec3 pos, float power, ResourceKey<Level> dimension) {
		this.position = pos;
		this.startTime = System.currentTimeMillis();
		this.power = power;
		this.durationTicks = (int) (power * 800);
		this.dimension = dimension;
		this.random = RandomSource.create();
	}

	public Vec3 getPosition() {
		return position;
	}

	public float getPower() {
		return power;
	}

	public ResourceKey<Level> getDimension() {
		return dimension;
	}

	public RandomSource random() {
		return random;
	}

	public long getStartTime() {
		return startTime;
	}

	public float getProgress() {
		long elapsed = System.currentTimeMillis() - startTime;
		float ticksElapsed = elapsed / 50.0f;
		return Math.min(1.0f, ticksElapsed / durationTicks);
	}

	public float getTime() {
		return (System.currentTimeMillis() - startTime) / 1000.0f;
	}

	public boolean isFinished() {
		return getProgress() >= 1.0f;
	}

	public float getIntensity() {
		float progress = getProgress();
		if (progress < 0.1f) {
			return progress / 0.1f;
		} else if (progress > 0.7f) {
			return 1.0f - (progress - 0.7f) / 0.3f;
		}
		return 1.0f;
	}

	public float getFlashIntensity() {
		float progress = getProgress();
		if (progress < 0.05f) {
			return (1.0f - progress / 0.05f) * 3.0f;
		}
		return 0.0f;
	}

	public float getShockwaveRadius() {
		return getProgress() * power * 50.0f;
	}

	public float getCloudHeight() {
		float progress = getProgress();
		float risePhase = Math.min(progress / 0.5f, 1.0f);
		return power * 30.0f * easeOutQuad(risePhase);
	}

	public float getCloudRadius() {
		float progress = getProgress();
		return power * 15.0f * (0.3f + 0.7f * easeOutQuad(Math.min(progress / 0.6f, 1.0f)));
	}

	private float easeOutQuad(float t) {
		return 1.0f - (1.0f - t) * (1.0f - t);
	}
}
