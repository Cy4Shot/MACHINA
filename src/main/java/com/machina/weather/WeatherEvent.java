package com.machina.weather;

import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;

public abstract class WeatherEvent {

	public static final Codec<WeatherEvent> CODEC = RegistryInit.WEATHER_EVENT.byNameCodec();
	public static final StreamCodec<RegistryFriendlyByteBuf, WeatherEvent> STREAM_CODEC = ByteBufCodecs
			.registry(RegistryInit.WEATHER_EVENT.key());

	private final int intensityTicks;
	private final IntProvider duration;
	private final ResourceLocation name;

	public WeatherEvent(ResourceLocation name, IntProvider duration, int intensityTicks) {
		this.duration = duration;
		this.name = name;
		this.intensityTicks = intensityTicks;
	}

	public int getDuration(RandomSource random) {
		return this.duration.sample(random);
	}

	public ResourceLocation getName() {
		return name;
	}

	public int getIntensityTicks() {
		return intensityTicks;
	}

	public int particleCount() {
		return 0;
	}

	public void spawnParticle(ClientLevel level, double posX, double posY, double posZ, double vX, double vY,
			double vZ) {

	}
}
