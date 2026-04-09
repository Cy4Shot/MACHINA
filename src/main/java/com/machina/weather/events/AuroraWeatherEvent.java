package com.machina.weather.events;

import com.machina.weather.WeatherEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;

public class AuroraWeatherEvent extends WeatherEvent {

	public AuroraWeatherEvent(ResourceLocation loc) {
		super(loc, UniformInt.of(12000, 24000), 100);
	}

}
