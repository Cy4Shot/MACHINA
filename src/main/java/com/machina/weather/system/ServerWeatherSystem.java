package com.machina.weather.system;

import java.util.List;

import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.network.s2c.S2CWeatherIntensityChange;
import com.machina.api.network.s2c.S2CWindDirectionChange;
import com.machina.api.util.PlanetHelper;
import com.machina.config.CommonConfig;
import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.network.PacketDistributor;

public class ServerWeatherSystem extends WeatherSystem {
	private static final float WIND_RESPONSE = 0.035F;
	private static final float WIND_TURBULENCE = 0.0008F;
	private static final float MAX_TARGET_TURN = (float) Math.toRadians(55.0F);
	private static final float MAX_TARGET_INTENSITY_DELTA = 0.22F;
	private static final int MIN_TARGET_HOLD_TICKS = 80;
	private static final int TARGET_HOLD_TICK_VARIANCE = 120;
	private static final float MIN_WIND_INTENSITY = 0.08F;
	private static final float MAX_WIND_INTENSITY = 1.20F;
	private static final int WIND_SYNC_INTERVAL = 10;
	private static final int WEATHER_INTENSITY_SYNC_INTERVAL = 5;

	private final List<WeatherEvent> allowedEvents;

	private WeatherEvent currentWeather;
	private int weatherTimer;
	private int weatherDuration;
	private int weatherAgeTicks;
	private float weatherIntensity;
	private float lastSentWeatherIntensity;
	private int weatherIntensitySyncTimer;
	private Vec2 windDirection;
	private Vec2 windTarget;
	private Vec2 lastSentWindDirection;
	private int windSyncTimer;
	private int windTargetTimer;

	public static record PersistenceState(ResourceLocation weatherEvent, int weatherTimer, int weatherDuration,
			int weatherAgeTicks, float weatherIntensity, float windX, float windZ, float windTargetX,
			float windTargetZ) {
	}

	public ServerWeatherSystem(ServerLevel level) {
		super(level);
		this.allowedEvents = PlanetHelper.getPlanetFor(level).type().weathers();
		this.currentWeather = WeatherEventInit.CLEAR.get();
		this.weatherDuration = currentWeather.getDuration(level.random);
		this.weatherTimer = this.weatherDuration;
		this.weatherAgeTicks = this.weatherDuration;
		this.weatherIntensity = 1.0F;
		this.lastSentWeatherIntensity = this.weatherIntensity;
		this.weatherIntensitySyncTimer = 0;
		this.windDirection = randomWindVector();
		this.windTarget = this.windDirection;
		this.lastSentWindDirection = this.windDirection;
		this.windSyncTimer = 0;
		this.windTargetTimer = 0;
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}

	public int getTicksRemaining() {
		return weatherTimer;
	}

	public PersistenceState createPersistenceState() {
		return new PersistenceState(currentWeather.getName(), weatherTimer, weatherDuration, weatherAgeTicks,
				weatherIntensity, windDirection.x, windDirection.y, windTarget.x, windTarget.y);
	}

	public void applyPersistenceState(PersistenceState state) {
		WeatherEvent resolved = resolveWeatherEvent(state.weatherEvent());
		this.currentWeather = resolved;
		this.weatherDuration = Math.max(state.weatherDuration(), 1);
		this.weatherTimer = Math.max(0, Math.min(state.weatherTimer(), this.weatherDuration));
		this.weatherAgeTicks = Math.max(state.weatherAgeTicks(), 0);
		this.weatherIntensity = Math.max(0.0F, Math.min(1.0F, state.weatherIntensity()));
		this.lastSentWeatherIntensity = this.weatherIntensity;
		this.weatherIntensitySyncTimer = 0;

		this.windDirection = normalizeLoadedWind(state.windX(), state.windZ());
		this.windTarget = normalizeLoadedWind(state.windTargetX(), state.windTargetZ());
		this.lastSentWindDirection = this.windDirection;
		this.windSyncTimer = 0;
		this.windTargetTimer = MIN_TARGET_HOLD_TICKS;
	}

	public float getWeatherIntensity() {
		return weatherIntensity;
	}

	@Override
	public Vec2 getWindDirection() {
		return windDirection;
	}

	@Override
	public void tick() {
		super.tick();
		weatherIntensitySyncTimer++;
		tickWind();
		windSyncTimer++;
		if (windSyncTimer >= WIND_SYNC_INTERVAL && hasMeaningfulWindDelta()) {
			windSyncTimer = 0;
			lastSentWindDirection = windDirection;
			PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWindDirectionChange(windDirection));
		}

		weatherAgeTicks++;
		if (level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			weatherTimer--;
			if (weatherTimer <= 0) {
				pickWeather(allowedEvents.get(level.random.nextInt(allowedEvents.size())));
			}
		}

		weatherIntensity = computeWeatherIntensity();
		if (shouldSyncWeatherIntensity()) {
			syncWeatherIntensity();
		}
	}

	private float computeWeatherIntensity() {
		int fadeTicks = CommonConfig.weatherFadeTicks.get();
		if (fadeTicks <= 0 || weatherDuration <= 0) {
			return 1.0F;
		}

		int elapsed = Math.max(weatherAgeTicks, 0);
		float fadeIn = elapsed >= fadeTicks ? 1.0F : elapsed / (float) fadeTicks;
		float fadeOut = 1.0F;
		if (level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			fadeOut = weatherTimer >= fadeTicks ? 1.0F : Math.max(weatherTimer, 0) / (float) fadeTicks;
		}
		return Math.min(fadeIn, fadeOut);
	}

	private boolean shouldSyncWeatherIntensity() {
		float delta = Math.abs(weatherIntensity - lastSentWeatherIntensity);
		if (weatherIntensitySyncTimer >= WEATHER_INTENSITY_SYNC_INTERVAL && delta >= 0.002F) {
			return true;
		}
		return delta >= 0.01F;
	}

	private void syncWeatherIntensity() {
		weatherIntensitySyncTimer = 0;
		lastSentWeatherIntensity = weatherIntensity;
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level,
				new S2CWeatherIntensityChange(weatherIntensity));
	}

	private void tickWind() {
		if (windTargetTimer <= 0) {
			pickNextWindTarget();
		} else {
			windTargetTimer--;
		}

		float toTargetX = windTarget.x - windDirection.x;
		float toTargetZ = windTarget.y - windDirection.y;
		float turbulenceX = (level.random.nextFloat() - 0.5F) * 2.0F * WIND_TURBULENCE;
		float turbulenceZ = (level.random.nextFloat() - 0.5F) * 2.0F * WIND_TURBULENCE;

		float nextX = windDirection.x + toTargetX * WIND_RESPONSE + turbulenceX;
		float nextZ = windDirection.y + toTargetZ * WIND_RESPONSE + turbulenceZ;
		float intensity = (float) Math.sqrt(nextX * nextX + nextZ * nextZ);

		if (intensity > MAX_WIND_INTENSITY) {
			float scale = MAX_WIND_INTENSITY / intensity;
			nextX *= scale;
			nextZ *= scale;
			intensity = MAX_WIND_INTENSITY;
		}

		if (intensity < MIN_WIND_INTENSITY) {
			float fallbackX = windTarget.x;
			float fallbackZ = windTarget.y;
			float fallbackLength = (float) Math.sqrt(fallbackX * fallbackX + fallbackZ * fallbackZ);
			if (fallbackLength < 1.0E-4F) {
				float angle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
				nextX = (float) Math.cos(angle) * MIN_WIND_INTENSITY;
				nextZ = (float) Math.sin(angle) * MIN_WIND_INTENSITY;
			} else {
				float scale = MIN_WIND_INTENSITY / fallbackLength;
				nextX = fallbackX * scale;
				nextZ = fallbackZ * scale;
			}
		}

		windDirection = new Vec2(nextX, nextZ);
	}

	private void pickNextWindTarget() {
		float currentIntensity = (float) Math
				.sqrt(windDirection.x * windDirection.x + windDirection.y * windDirection.y);
		if (currentIntensity < 1.0E-4F) {
			windTarget = randomWindVector();
			windTargetTimer = MIN_TARGET_HOLD_TICKS + level.random.nextInt(TARGET_HOLD_TICK_VARIANCE + 1);
			return;
		}

		float currentAngle = (float) Math.atan2(windDirection.y, windDirection.x);
		float turn = (level.random.nextFloat() * 2.0F - 1.0F) * MAX_TARGET_TURN;
		float nextAngle = currentAngle + turn;
		float intensityDelta = (level.random.nextFloat() * 2.0F - 1.0F) * MAX_TARGET_INTENSITY_DELTA;
		float targetIntensity = Math.max(MIN_WIND_INTENSITY,
				Math.min(MAX_WIND_INTENSITY, currentIntensity + intensityDelta));

		windTarget = new Vec2((float) Math.cos(nextAngle) * targetIntensity,
				(float) Math.sin(nextAngle) * targetIntensity);
		windTargetTimer = MIN_TARGET_HOLD_TICKS + level.random.nextInt(TARGET_HOLD_TICK_VARIANCE + 1);
	}

	private boolean hasMeaningfulWindDelta() {
		float dx = windDirection.x - lastSentWindDirection.x;
		float dz = windDirection.y - lastSentWindDirection.y;
		return dx * dx + dz * dz >= 0.0004F;
	}

	private Vec2 randomWindVector() {
		float angle = level.random.nextFloat() * ((float) Math.PI * 2.0F);
		float intensity = MIN_WIND_INTENSITY + level.random.nextFloat() * (MAX_WIND_INTENSITY - MIN_WIND_INTENSITY);
		return new Vec2((float) Math.cos(angle) * intensity, (float) Math.sin(angle) * intensity);
	}

	private Vec2 normalizeLoadedWind(float x, float z) {
		float length = (float) Math.sqrt(x * x + z * z);
		if (length < 1.0E-4F) {
			return randomWindVector();
		}
		if (length < MIN_WIND_INTENSITY) {
			float scale = MIN_WIND_INTENSITY / length;
			return new Vec2(x * scale, z * scale);
		}
		if (length > MAX_WIND_INTENSITY) {
			float scale = MAX_WIND_INTENSITY / length;
			return new Vec2(x * scale, z * scale);
		}
		return new Vec2(x, z);
	}

	private WeatherEvent resolveWeatherEvent(ResourceLocation id) {
		if (id != null) {
			for (WeatherEvent event : allowedEvents) {
				if (event.getName().equals(id)) {
					return event;
				}
			}
			if (WeatherEventInit.CLEAR.get().getName().equals(id)) {
				return WeatherEventInit.CLEAR.get();
			}
		}
		return WeatherEventInit.CLEAR.get();
	}

	public void setWindDirection(Vec2 windDirection) {
		this.windDirection = windDirection;
		this.windTarget = windDirection;
		this.lastSentWindDirection = windDirection;
		this.windSyncTimer = 0;
		this.windTargetTimer = MIN_TARGET_HOLD_TICKS;
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
		weatherDuration = currentWeather.getDuration(level.random);
		weatherTimer = weatherDuration;
		weatherAgeTicks = 0;
		weatherIntensity = computeWeatherIntensity();
		weatherIntensitySyncTimer = 0;
		lastSentWeatherIntensity = weatherIntensity;
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level, new S2CWeatherEventChange(currentWeather));
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level,
				new S2CWeatherIntensityChange(weatherIntensity));
	}
}
