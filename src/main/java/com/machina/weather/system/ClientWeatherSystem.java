package com.machina.weather.system;

import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.client.multiplayer.ClientLevel;

public class ClientWeatherSystem extends WeatherSystem {

	public ClientWeatherSystem(ClientLevel level) {
		super(level);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return WeatherEventInit.CLEAR.get();
	}

}
