package com.machina.registration.init;

import com.machina.Machina;
import com.machina.weather.WeatherEvent;
import com.machina.weather.events.ClearWeatherEvent;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WeatherEventInit {
	public static final DeferredRegister<WeatherEvent> WEATHER_EVENTS = DeferredRegister.create(RegistryInit.WEATHER_EVENT,
			Machina.MOD_ID);

	//@formatter:off
	public static final DeferredHolder<WeatherEvent, ClearWeatherEvent> CLEAR = WEATHER_EVENTS.register("clear", ClearWeatherEvent::new);
//	public static final Supplier<WeatherEvent> RAIN = WEATHER_EVENTS.register("rain", RainWeatherEvent::new);
	//@formatter:on
}
