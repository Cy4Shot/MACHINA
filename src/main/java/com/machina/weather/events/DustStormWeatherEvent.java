package com.machina.weather.events;

import com.machina.particle.DustStormParticleType.DustStormParticleOptions;
import com.machina.weather.WeatherEvent;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;

public class DustStormWeatherEvent extends WeatherEvent {

	public DustStormWeatherEvent(ResourceLocation loc) {
		super(loc, UniformInt.of(12000, 24000), 100);
	}

	@Override
	public int particleCount() {
		return 67;
	}

	@Override
	public void spawnParticle(ClientLevel level, double posX, double posY, double posZ, double vX, double vY, double vZ,
			int tint) {
		level.addParticle(ParticleTypes.ASH, posX, posY, posZ, vX, vY, vZ);
		if (level.random.nextFloat() < 0.1f) {
			level.addParticle(new DustStormParticleOptions(tint), posX - vX * 12, posY - vY * 12, posZ - vZ * 12, vX,
					vY, vZ);
		}
	}
	
	@Override
	public float getFogNear() {
		return 3f;
	}

	@Override
	public float getFogFar() {
		return 22f;
	}
	
	@Override
	public int getFogTint() {
		return 0xffe2ad;
	}
}
