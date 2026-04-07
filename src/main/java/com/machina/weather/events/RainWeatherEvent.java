package com.machina.weather.events;

import com.machina.weather.WeatherEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;

public class RainWeatherEvent extends WeatherEvent {
	public RainWeatherEvent(ResourceLocation loc) {
		super(loc, UniformInt.of(12000, 24000), 100);
	}

	@Override
	public float getFogNear() {
		return 10f;
	}

	@Override
	public float getFogFar() {
		return 100f;
	}
	
	@Override
	public int getFogTint() {
		return 0x68708c;
	}
}
