package com.machina.weather.spawners;

import com.machina.Machina;
import com.machina.api.util.PlanetHelper;
import com.machina.weather.WeatherEvent;
import com.machina.weather.manager.ClientWeatherManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
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

		WeatherEvent weather = ClientWeatherManager.getSystem(level).getCurrentEvent();
		if (weather == null || !weather.hasParticles())
			return;

		animateTick(level, weather, mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ());
	}

	private static void animateTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for (int j = 0; j < 667; j++) {
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos);
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos);
			doAnimateTick(level, weather, posX, posY, posZ, 16, blockpos$mutableblockpos);
			doAnimateTick(level, weather, posX, posY, posZ, 32, blockpos$mutableblockpos);
		}
	}

	private static void doAnimateTick(ClientLevel level, WeatherEvent weather, int posX, int posY, int posZ, int range,
			BlockPos.MutableBlockPos blockPos) {
		int i = posX + level.random.nextInt(range) - level.random.nextInt(range);
		int j = posY + level.random.nextInt(range) - level.random.nextInt(range);
		int k = posZ + level.random.nextInt(range) - level.random.nextInt(range);
		blockPos.set(i, j, k);
		BlockState blockstate = level.getBlockState(blockPos);
		if (!blockstate.isCollisionShapeFullBlock(level, blockPos)) {
			weather.spawnParticle(level, (double) blockPos.getX() + level.random.nextDouble(),
					(double) blockPos.getY() + level.random.nextDouble(),
					(double) blockPos.getZ() + level.random.nextDouble());
		}
	}
}
