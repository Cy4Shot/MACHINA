package com.machina.client.weather;

import com.machina.weather.WeatherEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;

public interface WeatherRenderer<T extends WeatherEvent> {

	static final Minecraft mc = Minecraft.getInstance();

	void renderWeather(ClientLevel level, float intensity, int ticks, float partialTick, LightTexture lightTexture,
			double camX, double camY, double camZ);
}
