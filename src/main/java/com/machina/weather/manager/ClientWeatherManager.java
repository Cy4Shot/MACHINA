package com.machina.weather.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.machina.api.util.PlanetHelper;
import com.machina.client.weather.WeatherRenderer;
import com.machina.weather.WeatherEvent;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;

//@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientWeatherManager {
	private static ClientWeatherSystem WEATHER;

	private static Map<WeatherEvent, WeatherRenderer<?>> RENDERERS = new HashMap<>();

	public static ClientWeatherSystem getSystem(ClientLevel level) {
		if (!PlanetHelper.isPlanetLevel(level.dimension())) {
			return null;
		}
		if (WEATHER == null || !WEATHER.getDimension().equals(level.dimension())) {
			WEATHER = new ClientWeatherSystem(level);
		}
		return WEATHER;
	}

	public static <T extends WeatherEvent> void registerRenderer(Supplier<T> event,
			Supplier<WeatherRenderer<T>> renderer) {
		RENDERERS.put(event.get(), renderer.get());
	}

	public static <T extends WeatherEvent> WeatherRenderer<?> getRenderer(T event) {
		return RENDERERS.get(event);
	}
}
