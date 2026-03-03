package com.machina.weather.system;

import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.server.level.ServerLevel;

public class ServerWeatherSystem extends WeatherSystem {

	public ServerWeatherSystem(ServerLevel level) {
		super(level);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return WeatherEventInit.CLEAR.get();
	}

}
