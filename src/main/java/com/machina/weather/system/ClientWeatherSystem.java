package com.machina.weather.system;

import com.machina.weather.WeatherEvent;

import net.minecraft.client.multiplayer.ClientLevel;

public class ClientWeatherSystem extends WeatherSystem {
	
	private WeatherEvent currentWeather;

	public ClientWeatherSystem(ClientLevel level) {
		super(level);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}
	
	public void setCurrentEvent(WeatherEvent event) {
		this.currentWeather = event;
	}

}
