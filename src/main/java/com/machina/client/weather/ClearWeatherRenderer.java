package com.machina.client.weather;

import com.machina.weather.events.ClearWeatherEvent;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;

public class ClearWeatherRenderer implements WeatherRenderer<ClearWeatherEvent> {

	@Override
	public void renderWeather(ClientLevel level, int ticks, float partialTick, LightTexture lightTexture, double camX,
			double camY, double camZ) {
		// DO NOTHING :)
	}

}
