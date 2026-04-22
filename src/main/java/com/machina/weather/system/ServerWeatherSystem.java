package com.machina.weather.system;

import java.util.List;

import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.network.s2c.S2CWeatherIntensityChange;
import com.machina.api.network.s2c.S2CTemperatureChange;
import com.machina.api.network.s2c.S2CWindDirectionChange;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_trait.PlanetTrait;
import com.machina.api.starchart.planet_trait.PlanetWeatherTrait;
import com.machina.api.util.PlanetHelper;
import com.machina.config.CommonConfig;
import com.machina.registration.init.WeatherEventInit;
import com.machina.weather.WeatherEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
	private static final float TWO_PI = (float) (Math.PI * 2.0D);
	private static final float DAY_NIGHT_NOON_OFFSET = 0.25F;
	private static final float MAX_VALID_TEMPERATURE = 2500.0F;
	private static final float MIN_ROUGH_TEMPERATURE_SPREAD = 1.0F;
	private static final float TEMPERATURE_SWING_MULT_DAILY = 2f;
	private static final float TEMPERATURE_SWING_MULT_SEASONAL = 6.7f;
	private static final double MC_DAY_TICKS = 24000.0D;
	private static final double MIN_SIM_DAY_TICKS = 6000.0D;
	private static final double MAX_SIM_DAY_TICKS = 240000.0D;
	private static final double MIN_SIM_SEASON_TICKS = MC_DAY_TICKS * 12.0D;
	private static final double MAX_SIM_SEASON_TICKS = MC_DAY_TICKS * 360.0D;
	private static final int TEMPERATURE_SYNC_INTERVAL = 20;

	private final List<WeatherEvent> allowedEvents;
	private final WeatherEvent forcedWeather;

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
	private float temperature;
	private float lastSentTemperature;
	private float referenceTemperature;
	private float dayNightTemperatureSwing;
	private float seasonalTemperatureSwing;
	private float roughMinTemperature;
	private float roughMaxTemperature;
	private int temperatureSyncTimer;

	public static record PersistenceState(ResourceLocation weatherEvent, int weatherTimer, int weatherDuration,
			int weatherAgeTicks, float weatherIntensity, float windX, float windZ, float windTargetX,
			float windTargetZ) {
	}

	public static record RoughTemperatureRange(float min, float max, float reference, float dayNightSwing,
			float seasonalSwing) {
	}

	public static RoughTemperatureRange calculateRoughTemperatureRange(Planet planet) {
		float referenceTemperature = resolveReferenceTemperature(planet);
		float dayNightTemperatureSwing = computeDayNightSwing(referenceTemperature, planet);
		float seasonalTemperatureSwing = computeSeasonalSwing(referenceTemperature, planet);
		float roughMinTemperature = Math.max(1.0F,
				referenceTemperature - dayNightTemperatureSwing - seasonalTemperatureSwing);
		float roughMaxTemperature = Math.max(roughMinTemperature + 0.1F,
				referenceTemperature + dayNightTemperatureSwing + seasonalTemperatureSwing);
		float baselineMinTemperature = roughMinTemperature;
		float baselineMaxTemperature = roughMaxTemperature;

		float hintedMin = isUsableTemperature(planet.min_temp()) ? (float) planet.min_temp() : Float.NaN;
		float hintedMax = isUsableTemperature(planet.max_temp()) ? (float) planet.max_temp() : Float.NaN;
		if (Float.isFinite(hintedMin) && Float.isFinite(hintedMax) && hintedMax > hintedMin) {
			roughMinTemperature = Math.max(roughMinTemperature, hintedMin);
			roughMaxTemperature = Math.min(roughMaxTemperature, hintedMax);
			if (roughMaxTemperature <= roughMinTemperature) {
				roughMinTemperature = hintedMin;
				roughMaxTemperature = hintedMax;
			}
		}

		if (roughMaxTemperature - roughMinTemperature < MIN_ROUGH_TEMPERATURE_SPREAD) {
			roughMinTemperature = baselineMinTemperature;
			roughMaxTemperature = baselineMaxTemperature;
		}

		if (roughMaxTemperature - roughMinTemperature < MIN_ROUGH_TEMPERATURE_SPREAD) {
			float midpoint = (roughMinTemperature + roughMaxTemperature) * 0.5F;
			roughMinTemperature = Math.max(1.0F, midpoint - (MIN_ROUGH_TEMPERATURE_SPREAD * 0.5F));
			roughMaxTemperature = roughMinTemperature + MIN_ROUGH_TEMPERATURE_SPREAD;
		}

		return new RoughTemperatureRange(roughMinTemperature, roughMaxTemperature, referenceTemperature,
				dayNightTemperatureSwing, seasonalTemperatureSwing);
	}

	public ServerWeatherSystem(ServerLevel level) {
		super(level);
		Planet planet = PlanetHelper.getPlanetFor(level);
		this.allowedEvents = planet.type().weathers();
		this.forcedWeather = resolveForcedWeather(planet);
		this.currentWeather = forcedWeather != null ? forcedWeather : WeatherEventInit.CLEAR.get();
		this.weatherDuration = currentWeather.getDuration(level.random);
		this.weatherTimer = this.weatherDuration;
		this.weatherAgeTicks = this.weatherDuration;
		this.weatherIntensity = 1.0F;
		this.lastSentWeatherIntensity = this.weatherIntensity;
		this.weatherIntensitySyncTimer = 0;
		RoughTemperatureRange temperatureRange = calculateRoughTemperatureRange(planet);
		this.referenceTemperature = temperatureRange.reference();
		this.dayNightTemperatureSwing = temperatureRange.dayNightSwing();
		this.seasonalTemperatureSwing = temperatureRange.seasonalSwing();
		this.roughMinTemperature = temperatureRange.min();
		this.roughMaxTemperature = temperatureRange.max();
		this.temperature = computeTemperature();
		this.lastSentTemperature = this.temperature;
		this.temperatureSyncTimer = 0;
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

	public int getWeatherDurationTicks() {
		return weatherDuration;
	}

	public int getWeatherAgeTicks() {
		return weatherAgeTicks;
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
		this.temperature = computeTemperature();
		this.lastSentTemperature = this.temperature;
		this.temperatureSyncTimer = 0;

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
	public float getTemperature() {
		return temperature;
	}

	@Override
	public float getRoughMinTemperature() {
		return roughMinTemperature;
	}

	@Override
	public float getRoughMaxTemperature() {
		return roughMaxTemperature;
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

		temperature = computeTemperature();
		temperatureSyncTimer++;
		if (shouldSyncTemperature()) {
			syncTemperature();
		}

		weatherAgeTicks++;
		if (level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			weatherTimer--;
			if (weatherTimer <= 0) {
				pickWeather(getNextWeatherEvent());
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

	private boolean shouldSyncTemperature() {
		float delta = Math.abs(temperature - lastSentTemperature);
		if (temperatureSyncTimer >= TEMPERATURE_SYNC_INTERVAL && delta >= 0.05F) {
			return true;
		}
		return delta >= 0.35F;
	}

	private void syncTemperature() {
		temperatureSyncTimer = 0;
		lastSentTemperature = temperature;
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level,
				new S2CTemperatureChange(temperature, roughMinTemperature, roughMaxTemperature));
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

	private float computeTemperature() {
		float daySignal = computeDayNightSignal();
		float seasonalSignal = computeSeasonalSignal();
		float current = referenceTemperature + daySignal * dayNightTemperatureSwing
				+ seasonalSignal * seasonalTemperatureSwing;
		return Mth.clamp(current, roughMinTemperature, roughMaxTemperature);
	}

	private float computeDayNightSignal() {
		double dayTicks = getSimulatedDayTicks();
		double dayPhase = (level.getGameTime() % dayTicks) / dayTicks;
		return (float) Math.cos((dayPhase - DAY_NIGHT_NOON_OFFSET) * TWO_PI);
	}

	private float computeSeasonalSignal() {
		double seasonTicks = getSimulatedSeasonTicks();
		double meanMotion = (Math.PI * 2.0D) / seasonTicks;
		double meanAnomaly = meanMotion * level.getGameTime() + planet.where_in_orbit();
		double trueAnomaly = planet.trueAnomalyFromMean(meanAnomaly, Math.min(Math.max(planet.e(), 0.0D), 0.99D));
		double phaseBias = Math.toRadians(planet.axial_tilt()) * 0.25D;
		return (float) Math.sin(trueAnomaly + phaseBias);
	}

	private double getSimulatedDayTicks() {
		double dayHours = planet.day();
		if (!Double.isFinite(dayHours) || dayHours <= 0.0D) {
			dayHours = 24.0D;
		}
		double rawTicks = (dayHours / 24.0D) * MC_DAY_TICKS;
		return Mth.clamp(rawTicks, MIN_SIM_DAY_TICKS, MAX_SIM_DAY_TICKS);
	}

	private double getSimulatedSeasonTicks() {
		double periodDays = planet.orb_period();
		if (!Double.isFinite(periodDays) || periodDays <= 0.0D) {
			periodDays = 365.0D;
		}
		double rawTicks = periodDays * MC_DAY_TICKS;
		return Mth.clamp(rawTicks, MIN_SIM_SEASON_TICKS, MAX_SIM_SEASON_TICKS);
	}

	private static float resolveReferenceTemperature(Planet planet) {
		if (isUsableTemperature(planet.surf_temp())) {
			return (float) planet.surf_temp();
		}
		if (isUsableTemperature(planet.avg_temp())) {
			return (float) planet.avg_temp();
		}
		if (isUsableTemperature(planet.min_temp()) && isUsableTemperature(planet.max_temp())
				&& planet.max_temp() > planet.min_temp()) {
			return (float) ((planet.min_temp() + planet.max_temp()) * 0.5D);
		}
		return Mth.clamp((float) planet.surf_temp(), 120.0F, 700.0F);
	}

	private static float computeDayNightSwing(float baseTemperature, Planet planet) {
		float pct = planet.gas_giant() ? 0.015F : 0.03F;
		return Mth.clamp(baseTemperature * pct, 1.0F, planet.gas_giant() ? 12.0F : 24.0F)
				* TEMPERATURE_SWING_MULT_DAILY;
	}

	private static float computeSeasonalSwing(float baseTemperature, Planet planet) {
		float axialTiltFactor = Mth.clamp(Math.abs(planet.axial_tilt()) / 90.0F, 0.0F, 1.0F);
		float eccentricityFactor = Mth.clamp((float) planet.e(), 0.0F, 0.8F);
		float pct = 0.01F + axialTiltFactor * 0.04F + eccentricityFactor * 0.05F;
		if (planet.gas_giant()) {
			pct *= 0.75F;
		}
		return Mth.clamp(baseTemperature * pct, 1.5F, planet.gas_giant() ? 20.0F : 38.0F)
				* TEMPERATURE_SWING_MULT_SEASONAL;
	}

	private static boolean isUsableTemperature(double value) {
		return Double.isFinite(value) && value > 1.0D && value < MAX_VALID_TEMPERATURE;
	}

	private WeatherEvent resolveWeatherEvent(ResourceLocation id) {
		if (forcedWeather != null) {
			return forcedWeather;
		}
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
		if (forcedWeather != null) {
			event = forcedWeather;
		}
		currentWeather = event;
		weatherDuration = currentWeather.getDuration(level.random);
		weatherTimer = weatherDuration;
		weatherAgeTicks = 0;
		weatherIntensity = computeWeatherIntensity();
		weatherIntensitySyncTimer = 0;
		lastSentWeatherIntensity = weatherIntensity;
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level,
				new S2CWeatherEventChange(currentWeather, weatherDuration, weatherTimer, weatherAgeTicks));
		PacketDistributor.sendToPlayersInDimension((ServerLevel) level,
				new S2CWeatherIntensityChange(weatherIntensity));
		syncTemperature();
	}

	private WeatherEvent getNextWeatherEvent() {
		if (forcedWeather != null) {
			return forcedWeather;
		}
		if (allowedEvents.isEmpty()) {
			return WeatherEventInit.CLEAR.get();
		}
		return allowedEvents.get(level.random.nextInt(allowedEvents.size()));
	}

	private WeatherEvent resolveForcedWeather(Planet planet) {
		for (PlanetTrait trait : planet.traits()) {
			if (trait instanceof PlanetWeatherTrait weather) {
				return weather.weather();
			}
		}
		return null;
	}
}
