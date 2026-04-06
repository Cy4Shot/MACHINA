package com.machina.weather.system;

import com.machina.weather.WeatherEvent;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec2;

public class ClientWeatherSystem extends WeatherSystem {
	
	private WeatherEvent currentWeather;
	private Vec2 windDirection;

	public ClientWeatherSystem(ClientLevel level) {
		super(level);
		this.windDirection = new Vec2(0.0F, 0.0F);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}
	
	public void setCurrentEvent(WeatherEvent event) {
		this.currentWeather = event;
	}

	@Override
	public Vec2 getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Vec2 windDirection) {
		this.windDirection = windDirection;
	}

}
