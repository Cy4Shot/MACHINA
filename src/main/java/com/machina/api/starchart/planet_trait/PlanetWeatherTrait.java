package com.machina.api.starchart.planet_trait;

import java.util.function.Supplier;

import com.machina.weather.WeatherEvent;

public class PlanetWeatherTrait extends PlanetTrait {

	private final Supplier<WeatherEvent> weather;

	public PlanetWeatherTrait(String name, int color, Supplier<WeatherEvent> weather) {
		super(name, color);
		this.weather = weather;
	}

	public WeatherEvent weather() {
		return this.weather.get();
	}
}
