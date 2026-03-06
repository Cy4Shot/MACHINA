package com.machina.weather.system;

import java.util.List;

import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.util.PlanetHelper;
import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.neoforged.neoforge.network.PacketDistributor;

public class ServerWeatherSystem extends WeatherSystem {

	private final List<WeatherEvent> allowedEvents;

	private WeatherEvent currentWeather;
	private int weatherTimer;

	public ServerWeatherSystem(ServerLevel level) {
		super(level);
		this.allowedEvents = PlanetHelper.getPlanetFor(level).type().weathers();
		this.currentWeather = WeatherEventInit.CLEAR.get();
		this.weatherTimer = currentWeather.getDuration(level.random);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}
	
	public int getTicksRemaining() {
		return weatherTimer;
	}

	@Override
	public void tick() {
		super.tick();
		if (level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			weatherTimer--;
			if (weatherTimer <= 0) {
				pickWeather(allowedEvents.get(level.random.nextInt(allowedEvents.size())));
			}
		}
	}

	public void pickWeather(WeatherEvent event) {
		currentWeather = event;
		weatherTimer = currentWeather.getDuration(level.random);
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWeatherEventChange(currentWeather));
	}
}
