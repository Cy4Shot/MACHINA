package com.machina.weather.events;

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
	public boolean hasParticles() {
		return true;
	}

	@Override
	public void spawnParticle(ClientLevel level, double posX, double posY, double posZ, double vX, double vY, double vZ) {
		if (level.random.nextFloat() < 0.01f) {
			level.addParticle(ParticleTypes.CLOUD, posX, posY, posZ, vX, vY, vZ);
		}
		if (level.random.nextFloat() < 0.1f) {
			level.addParticle(ParticleTypes.ASH, posX, posY, posZ, vX, vY, vZ);
		}
		if (level.random.nextFloat() < 0.2f) {
			level.addParticle(ParticleTypes.DUST_PLUME, posX, posY, posZ, vX, vY, vZ);
		}
		if (level.random.nextFloat() < 0.0001f) {
			level.addParticle(ParticleTypes.GUST_EMITTER_SMALL, posX, posY, posZ, vX, vY, vZ);
		}
	}
}
