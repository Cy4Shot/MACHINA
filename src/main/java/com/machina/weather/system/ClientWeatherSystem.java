package com.machina.weather.system;

import com.machina.Machina;
import com.machina.api.client.ClientBiomeSettings;
import com.machina.api.client.ClientStarchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.api.util.PlanetHelper;
import com.machina.client.PlanetSpecialEffects;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientWeatherSystem extends WeatherSystem {

	private WeatherEvent currentWeather;
	private Vec2 windDirection;
	private float weatherIntensity;
	private int weatherDurationTicks;
	private int weatherRemainingTicks;
	private int weatherAgeTicks;
	private long weatherTimelineGameTime;
	private float temperature;
	private float roughMinTemperature;
	private float roughMaxTemperature;

	public ClientWeatherSystem(ClientLevel level) {
		super(level);
		this.windDirection = new Vec2(0.0F, 0.0F);
		this.weatherIntensity = 1.0F;
		this.weatherDurationTicks = 0;
		this.weatherRemainingTicks = 0;
		this.weatherAgeTicks = 0;
		this.weatherTimelineGameTime = level.getGameTime();
		this.temperature = 273.0F;
		this.roughMinTemperature = 263.0F;
		this.roughMaxTemperature = 283.0F;
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}

	public void setCurrentEvent(WeatherEvent event) {
		this.currentWeather = event;
	}

	public int getWeatherDurationTicks() {
		return weatherDurationTicks;
	}

	public int getWeatherRemainingTicks() {
		return weatherRemainingTicks;
	}

	public int getWeatherAgeTicks() {
		return weatherAgeTicks;
	}

	public void setWeatherTimeline(int durationTicks, int remainingTicks, int ageTicks) {
		this.weatherDurationTicks = Math.max(0, durationTicks);
		this.weatherRemainingTicks = Math.max(0, remainingTicks);
		this.weatherAgeTicks = Math.max(0, ageTicks);
		this.weatherTimelineGameTime = level.getGameTime();
	}

	public void tickWeatherTimeline() {
		if (currentWeather == null) {
			return;
		}
		long currentGameTime = level.getGameTime();
		int elapsedTicks = (int) Math.max(0L, currentGameTime - weatherTimelineGameTime);
		if (elapsedTicks <= 0) {
			return;
		}
		weatherTimelineGameTime = currentGameTime;

		if (weatherDurationTicks > 0) {
			weatherAgeTicks += elapsedTicks;
		}
		if (weatherRemainingTicks > 0 && level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
			weatherRemainingTicks = Math.max(0, weatherRemainingTicks - elapsedTicks);
		}
	}

	public float getWeatherIntensity() {
		return weatherIntensity;
	}

	public void setWeatherIntensity(float weatherIntensity) {
		this.weatherIntensity = Mth.clamp(weatherIntensity, 0.0F, 1.0F);
	}

	@Override
	public Vec2 getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Vec2 windDirection) {
		this.windDirection = windDirection;
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

	public void setTemperatureData(float temperature, float roughMinTemperature, float roughMaxTemperature) {
		float min = Math.min(roughMinTemperature, roughMaxTemperature);
		float max = Math.max(roughMinTemperature, roughMaxTemperature);
		if (!Float.isFinite(min) || !Float.isFinite(max) || max <= min) {
			return;
		}
		this.roughMinTemperature = min;
		this.roughMaxTemperature = max;
		this.temperature = Mth.clamp(temperature, min, max);
	}

	@SubscribeEvent
	public static final void onFogRender(final ViewportEvent.RenderFog event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null || !mc.level.tickRateManager().runsNormally() || mc.isPaused())
			return;

		ClientLevel level = mc.level;
		Planet planet = ClientStarchart.getPlanet(level);
		if (planet == null)
			return;

		ClientWeatherSystem system = ClientWeatherManager.getSystem(level);
		if (system == null)
			return;

		WeatherEvent weather = system.getCurrentEvent();
		float weatherIntensity = system.getWeatherIntensity();
		if (weather == null || weather.getFogFar() == 0f || weatherIntensity <= 0.0F)
			return;

		float partialTick = (float) event.getPartialTick();
		Vec3 skyColor = PlanetSpecialEffects.getCachedSkyColor(level, planet, partialTick);
		Vec3 fogColor = PlanetSpecialEffects.getCachedWeatherFogColor(level, weather, partialTick, skyColor);
		if (fogColor == null) {
			fogColor = Vec3.ZERO;
		}

		float near = Mth.lerp(weatherIntensity, 0.0F, weather.getFogNear());
		float far = Mth.lerp(weatherIntensity, 1024.0F, weather.getFogFar());
		if (event.getMode() == FogMode.FOG_SKY) {
			RenderSystem.setShaderFogStart(0.0F);
			RenderSystem.setShaderFogEnd(far * 0.8F);
		} else {
			RenderSystem.setShaderFogStart(near);
			RenderSystem.setShaderFogEnd(far);
		}
		RenderSystem.setShaderFogColor((float) fogColor.x, (float) fogColor.y, (float) fogColor.z);
		RenderSystem.setShaderFogShape(FogShape.SPHERE);
	}

	@SubscribeEvent
	public static final void onLevelTick(final LevelTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.getLevel() == null || !event.getLevel().isClientSide() || mc.player == null
				|| !event.getLevel().tickRateManager().runsNormally() || mc.isPaused())
			return;

		ClientLevel level = (ClientLevel) event.getLevel();
		if (!PlanetHelper.isPlanetLevel(level.dimension()))
			return;

		ClientWeatherSystem system = ClientWeatherManager.getSystem(level);
		if (system == null)
			return;

		system.tickWeatherTimeline();

		WeatherEvent weather = system.getCurrentEvent();
		if (weather == null || weather.particleCount() == 0 || system.getWeatherIntensity() <= 0.0F)
			return;

		Vec2 wind = system.getWindDirection();
		animateParticleTick(level, weather, system.getWeatherIntensity(), mc.player.getBlockX(), mc.player.getBlockY(),
				mc.player.getBlockZ(), wind.x, 0, wind.y);
	}

	private static void animateParticleTick(ClientLevel level, WeatherEvent weather, float intensity, int posX,
			int posY, int posZ, double vX, double vY, double vZ) {
		ResourceLocation biome = level.getBiome(new BlockPos(posX, posY, posZ)).getKey().location();
		int tint = ClientBiomeSettings.BIOME_SETTINGS.getOrDefault(biome, PlanetBiomeClientSettings.DEFAULT)
				.weather_tint();
		BlockPos.MutableBlockPos mutpos = new BlockPos.MutableBlockPos();
		int count = Math.max(0, Math.round(weather.particleCount() * intensity));
		for (int j = 0; j < count; j++) {
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, mutpos, vX, vY, vZ, 0.05, tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, mutpos, vX, vY, vZ, 0.01, tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, mutpos, vX, vY, vZ, 0.01, tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 32, mutpos, vX, vY, vZ, 0.0, tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 64, mutpos, vX, vY, vZ, 0.0, tint);
		}
	}

	private static void doAnimateParticleTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ,
			int range, BlockPos.MutableBlockPos blockPos, double vX, double vY, double vZ, double var, int tint) {
		int i = posX + level.random.nextInt(range) - level.random.nextInt(range);
		int j = posY + level.random.nextInt(range) - level.random.nextInt(range);
		int k = posZ + level.random.nextInt(range) - level.random.nextInt(range);
		if (j < level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k)) {
			return;
		}
		double x = vX + level.random.nextDouble() * var - level.random.nextDouble() * var;
		double y = vY + level.random.nextDouble() * var - level.random.nextDouble() * var;
		double z = vZ + level.random.nextDouble() * var - level.random.nextDouble() * var;
		blockPos.set(i, j, k);
		BlockState blockstate = level.getBlockState(blockPos);
		if (!blockstate.isCollisionShapeFullBlock(level, blockPos)) {
			weather.spawnParticle(level, (double) blockPos.getX() + level.random.nextDouble(),
					(double) blockPos.getY() + level.random.nextDouble(),
					(double) blockPos.getZ() + level.random.nextDouble(), x, y, z, tint);
		}
	}
}
