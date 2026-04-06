package com.machina.weather.system;

import java.util.List;

import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.network.s2c.S2CWindDirectionChange;
import com.machina.api.util.PlanetHelper;
import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.network.PacketDistributor;

public class ServerWeatherSystem extends WeatherSystem {
	private static final float WIND_DRAG = 0.992F;
	private static final float GUST_MIN = 0.001F;
	private static final float GUST_RANGE = 0.005F;
	private static final float MIN_WIND_INTENSITY = 0.08F;
	private static final float MAX_WIND_INTENSITY = 1.20F;
	private static final int WIND_SYNC_INTERVAL = 10;

	private final List<WeatherEvent> allowedEvents;

	private WeatherEvent currentWeather;
	private int weatherTimer;
	private Vec2 windDirection;
	private Vec2 lastSentWindDirection;
	private int windSyncTimer;

	public ServerWeatherSystem(ServerLevel level) {
		super(level);
		this.allowedEvents = PlanetHelper.getPlanetFor(level).type().weathers();
		this.currentWeather = WeatherEventInit.CLEAR.get();
		this.weatherTimer = currentWeather.getDuration(level.random);
		this.windDirection = randomWindVector();
		this.lastSentWindDirection = this.windDirection;
		this.windSyncTimer = 0;
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}
	
	public int getTicksRemaining() {
		return weatherTimer;
	}

	@Override
	public Vec2 getWindDirection() {
		return windDirection;
	}

	@Override
	public void tick() {
		super.tick();
		tickWind();
		windSyncTimer++;
		if (windSyncTimer >= WIND_SYNC_INTERVAL && hasMeaningfulWindDelta()) {
			windSyncTimer = 0;
			lastSentWindDirection = windDirection;
			PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWindDirectionChange(windDirection));
		}

		if (level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			weatherTimer--;
			if (weatherTimer <= 0) {
				pickWeather(allowedEvents.get(level.random.nextInt(allowedEvents.size())));
			}
		}
	}

	private void tickWind() {
		float gustStrength = GUST_MIN + level.random.nextFloat() * GUST_RANGE;
		float gustAngle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
		float gustX = (float) Math.cos(gustAngle) * gustStrength;
		float gustZ = (float) Math.sin(gustAngle) * gustStrength;

		float nextX = windDirection.x * WIND_DRAG + gustX;
		float nextZ = windDirection.y * WIND_DRAG + gustZ;
		float intensity = (float) Math.sqrt(nextX * nextX + nextZ * nextZ);

		if (intensity > MAX_WIND_INTENSITY) {
			float scale = MAX_WIND_INTENSITY / intensity;
			nextX *= scale;
			nextZ *= scale;
			intensity = MAX_WIND_INTENSITY;
		}

		if (intensity < MIN_WIND_INTENSITY) {
			float angle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
			nextX += (float) Math.cos(angle) * (MIN_WIND_INTENSITY - intensity);
			nextZ += (float) Math.sin(angle) * (MIN_WIND_INTENSITY - intensity);
		}

		windDirection = new Vec2(nextX, nextZ);
	}

	private boolean hasMeaningfulWindDelta() {
		float dx = windDirection.x - lastSentWindDirection.x;
		float dz = windDirection.y - lastSentWindDirection.y;
		return dx * dx + dz * dz >= 0.0004F;
	}

	private Vec2 randomWindVector() {
		float angle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
		float intensity = MIN_WIND_INTENSITY
				+ level.random.nextFloat() * (MAX_WIND_INTENSITY - MIN_WIND_INTENSITY);
		return new Vec2((float) Math.cos(angle) * intensity, (float) Math.sin(angle) * intensity);
	}

	public void setWindDirection(Vec2 windDirection) {
		this.windDirection = windDirection;
		this.lastSentWindDirection = windDirection;
		this.windSyncTimer = 0;
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWindDirectionChange(windDirection));
	}

	public void setWindIntensity(float intensity) {
		float clamped = Math.max(MIN_WIND_INTENSITY, Math.min(MAX_WIND_INTENSITY, intensity));
		float currentLength = (float) Math.sqrt(windDirection.x * windDirection.x + windDirection.y * windDirection.y);
		if (currentLength < 1.0E-4F) {
			float angle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
			setWindDirection(new Vec2((float) Math.cos(angle) * clamped, (float) Math.sin(angle) * clamped));
			return;
		}
		float scale = clamped / currentLength;
		setWindDirection(new Vec2(windDirection.x * scale, windDirection.y * scale));
	}

	public void pickWeather(WeatherEvent event) {
		currentWeather = event;
		weatherTimer = currentWeather.getDuration(level.random);
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWeatherEventChange(currentWeather));
	}
}
