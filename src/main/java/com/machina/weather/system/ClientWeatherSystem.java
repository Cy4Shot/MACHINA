package com.machina.weather.system;

import com.machina.Machina;
import com.machina.api.client.ClientBiomeSettings;
import com.machina.api.client.ClientStarchart;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.api.util.PlanetHelper;
import com.machina.api.util.math.ColorUtil;
import com.machina.api.util.math.ColorUtil.RGBA;
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

	public ClientWeatherSystem(ClientLevel level) {
		super(level);
		this.windDirection = new Vec2(0.0F, 0.0F);
	}

	@Override
	public WeatherEvent getCurrentEvent() {
		return currentWeather;
	}

	public void setCurrentEvent(WeatherEvent event) {
		this.currentWeather = event;
	}

	@Override
	public Vec2 getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Vec2 windDirection) {
		this.windDirection = windDirection;
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
		if (weather == null || weather.getFogFar() == 0f)
			return;

		// 1. Apply biome tint
		ResourceLocation biome = level.getBiome(mc.player.blockPosition()).getKey().location();
		RGBA tint = ColorUtil.ofRGBA(ClientBiomeSettings.BIOME_SETTINGS
				.getOrDefault(biome, PlanetBiomeClientSettings.DEFAULT).weather_tint());
		
		// 2. Apply weather tint
		tint = tint.mul(ColorUtil.ofRGBA(weather.getFogTint()));

		// 3. Apply sky tint
		Vec3 skyColor = PlanetSpecialEffects.getSkyColor(planet, mc.cameraEntity.position(),
				level.getTimeOfDay((float) event.getPartialTick()));
		if (skyColor != null) {
			tint = tint.mul(skyColor);
		}

		float near = weather.getFogNear();
		float far = weather.getFogFar();
		if (event.getMode() == FogMode.FOG_SKY) {
			RenderSystem.setShaderFogStart(0.0F);
			RenderSystem.setShaderFogEnd(far * 0.8F);
		} else {
			RenderSystem.setShaderFogStart(near);
			RenderSystem.setShaderFogEnd(far);
		}
		tint.setRenderSystemFogColor();
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

		WeatherEvent weather = system.getCurrentEvent();
		if (weather == null || weather.particleCount() == 0)
			return;

		Vec2 wind = system.getWindDirection();
		animateParticleTick(level, weather, mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ(), wind.x,
				0, wind.y);
	}

	private static void animateParticleTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ,
			double vX, double vY, double vZ) {
		ResourceLocation biome = level.getBiome(new BlockPos(posX, posY, posZ)).getKey().location();
		int tint = ClientBiomeSettings.BIOME_SETTINGS.getOrDefault(biome, PlanetBiomeClientSettings.DEFAULT)
				.weather_tint();
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (int j = 0; j < weather.particleCount(); j++) {
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.05,
					tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.01,
					tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.01,
					tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 32, blockpos$mutableblockpos, vX, vY, vZ, 0.0,
					tint);
			doAnimateParticleTick(level, weather, posX, posY, posZ, 64, blockpos$mutableblockpos, vX, vY, vZ, 0.0,
					tint);
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
