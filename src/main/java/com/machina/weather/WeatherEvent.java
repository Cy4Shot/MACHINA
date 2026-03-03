package com.machina.weather;

import com.machina.registration.init.RegistryInit;
import com.mojang.serialization.Codec;

public abstract class WeatherEvent {
	
	public static final Codec<WeatherEvent> CODEC = RegistryInit.WEATHER_EVENT.byNameCodec();

}
