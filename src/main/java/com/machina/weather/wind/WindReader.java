package com.machina.weather.wind;

import com.machina.weather.WeatherManager;
import com.machina.weather.event.WeatherClientEvents;
import com.machina.weather.event.WeatherServerEvents;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WindReader {
	public static float getWindAngle(World world) {
		WeatherManager weather = getWeatherManagerFor(world);
		return weather != null ? weather.wind.getWindAngle() : 0;
	}

	public static float getWindSpeed(World world) {
		WeatherManager weather = getWeatherManagerFor(world);
		return weather != null ? weather.wind.getWindSpeed() : 0;
	}

	private static WeatherManager getWeatherManagerFor(World world) {
		if (world.isClientSide) {
			return getWeatherManagerClient();
		} else {
			return WeatherServerEvents.getWeatherManagerFor((world.dimension()));
		}
	}
	
	public static float getWindAngle(RegistryKey<World> world, boolean client) {
		WeatherManager weather = getWeatherManagerFor(world, client);
		return weather != null ? weather.wind.getWindAngle() : 0;
	}

	public static float getWindSpeed(RegistryKey<World> world, boolean client) {
		WeatherManager weather = getWeatherManagerFor(world, client);
		return weather != null ? weather.wind.getWindSpeed() : 0;
	}

	private static WeatherManager getWeatherManagerFor(RegistryKey<World> world, boolean client) {
		if (client) {
			return getWeatherManagerClient();
		} else {
			return WeatherServerEvents.getWeatherManagerFor(world);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static WeatherManager getWeatherManagerClient() {
		return WeatherClientEvents.weatherManager;
	}
}
