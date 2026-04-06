package com.machina.weather.spawners;

import com.machina.Machina;
import com.machina.api.util.PlanetHelper;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class WeatherAmbientParticles {

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
		if (weather == null || !weather.hasParticles())
			return;

		Vec2 wind = system.getWindDirection();
		animateTick(level, weather, mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ(), wind.x, 0,
				wind.y);
	}

	private static void animateTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ, double vX,
			double vY, double vZ) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (int j = 0; j < 667; j++) {
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.2);
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.1);
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos, vX, vY, vZ, 0.1);
			doAnimateTick(level, weather, posX, posY, posZ, 32, blockpos$mutableblockpos, vX, vY, vZ, 0.0);
		}
	}

	private static void doAnimateTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ, int range,
			BlockPos.MutableBlockPos blockPos, double vX, double vY, double vZ, double var) {
		int i = posX + level.random.nextInt(range) - level.random.nextInt(range);
		int j = posY + level.random.nextInt(range) - level.random.nextInt(range);
		int k = posZ + level.random.nextInt(range) - level.random.nextInt(range);
		double x = vX + level.random.nextDouble() * var - level.random.nextDouble() * var;
		double y = vY + level.random.nextDouble() * var - level.random.nextDouble() * var;
		double z = vZ + level.random.nextDouble() * var - level.random.nextDouble() * var;
		blockPos.set(i, j, k);
		BlockState blockstate = level.getBlockState(blockPos);
		if (!blockstate.isCollisionShapeFullBlock(level, blockPos)) {
			weather.spawnParticle(level, (double) blockPos.getX() + level.random.nextDouble(),
					(double) blockPos.getY() + level.random.nextDouble(),
					(double) blockPos.getZ() + level.random.nextDouble(), x, y, z);
		}
	}
}
