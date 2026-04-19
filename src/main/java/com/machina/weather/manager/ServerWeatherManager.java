package com.machina.weather.manager;

import java.util.HashMap;
import java.util.Map;

import com.machina.Machina;
import com.machina.api.network.s2c.S2CWeatherEventChange;
import com.machina.api.network.s2c.S2CWeatherIntensityChange;
import com.machina.api.network.s2c.S2CWindDirectionChange;
import com.machina.api.util.PlanetHelper;
import com.machina.weather.system.ServerWeatherSystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class ServerWeatherManager {

	private static final Map<Integer, ServerWeatherSystem> WEATHERS = new HashMap<>();

	public static ServerWeatherSystem getOrCreate(ServerLevel level) {
		int id = PlanetHelper.getIdLevelOr(level.dimension(), -1);
		if (id != -1) {
			return WEATHERS.computeIfAbsent(id, (x) -> new ServerWeatherSystem(level));
		}
		return null;
	}

	@SubscribeEvent
	public static void onTick(final LevelTickEvent.Post event) {
		if (event.getLevel().isClientSide()) {
			return;
		}

		ServerWeatherSystem weather = getOrCreate((ServerLevel) event.getLevel());
		if (weather != null) {
			weather.tick();
		}
	}

	@SubscribeEvent
	public static void onLoad(final EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide()) {
			return;
		}

		ServerWeatherSystem weather = getOrCreate((ServerLevel) event.getLevel());
		if (weather != null && event.getEntity() instanceof ServerPlayer player) {
			PacketDistributor.sendToPlayer(player, new S2CWeatherEventChange(weather.getCurrentEvent()));
			PacketDistributor.sendToPlayer(player, new S2CWeatherIntensityChange(weather.getWeatherIntensity()));
			PacketDistributor.sendToPlayer(player, new S2CWindDirectionChange(weather.getWindDirection()));
		}
	}
}
