package com.machina.weather.wind;

import java.util.Random;

import com.machina.weather.WeatherManager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class WindManager {
	public WeatherManager manager;

	// global
	public float windAngleGlobal = 0;
	public float windSpeedGlobal = 0;

	public WindManager(WeatherManager parManager) {
		manager = parManager;
		Random rand = new Random();
		windAngleGlobal = rand.nextInt(360);
	}

	public float getWindSpeed() {
		return windSpeedGlobal;

	}

	public float getWindAngle() {
		return windAngleGlobal;
	}

	public void tick(World world) {
		Random rand = new Random();

		windAngleGlobal += rand.nextFloat() - rand.nextFloat();
		if (windAngleGlobal < -180) {
			windAngleGlobal += 360;
		}

		if (windAngleGlobal > 180) {
			windAngleGlobal -= 360;
		}
	}

	/**
	 * Handle generic uses of wind force, for stuff like weather objects that arent
	 * entities or paticles
	 */
	public Vector3f applyWindForceImpl(Vector3f motion, float weight, float multiplier, float maxSpeed) {
		float windSpeed = getWindSpeed();
		float windAngle = getWindAngle();

		float windX = (float) -Math.sin(Math.toRadians(windAngle)) * windSpeed;
		float windZ = (float) Math.cos(Math.toRadians(windAngle)) * windSpeed;

		float objX = (float) motion.x();
		float objZ = (float) motion.z();

		float windWeight = 1F;
		float objWeight = weight;

		// divide by zero protection
		if (objWeight <= 0) {
			objWeight = 0.001F;
		}

		float weightDiff = windWeight / objWeight;

		float vecX = (objX - windX) * weightDiff;
		float vecZ = (objZ - windZ) * weightDiff;

		vecX *= multiplier;
		vecZ *= multiplier;

		// copy over existing motion data
		Vector3f newMotion = motion.copy();

		double speedCheck = (Math.abs(vecX) + Math.abs(vecZ)) / 2D;
		if (speedCheck < maxSpeed) {
			newMotion = new Vector3f(objX - vecX, motion.y(), objZ - vecZ);
		} else {
			float speedDampen = (float) (maxSpeed / speedCheck);
			newMotion = new Vector3f(objX - vecX * speedDampen, motion.y(), objZ - vecZ * speedDampen);
		}

		return newMotion;
	}

	public CompoundNBT nbtSyncForClient() {
		CompoundNBT data = new CompoundNBT();
		data.putFloat("windSpeedGlobal", windSpeedGlobal);
		data.putFloat("windAngleGlobal", windAngleGlobal);
		return data;
	}

	public void nbtSyncFromServer(CompoundNBT parNBT) {
		windSpeedGlobal = parNBT.getFloat("windSpeedGlobal");
		windAngleGlobal = parNBT.getFloat("windAngleGlobal");
	}

	public Vector3f getWindForce() {
		float windSpeed = this.getWindSpeed();
		float windAngle = this.getWindAngle();
		float windX = (float) -Math.sin(Math.toRadians(windAngle)) * windSpeed;
		float windZ = (float) Math.cos(Math.toRadians(windAngle)) * windSpeed;
		return new Vector3f(windX, 0, windZ);
	}
}