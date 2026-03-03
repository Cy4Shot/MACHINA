package com.machina.weather.manager;

import com.machina.Machina;
import com.machina.api.util.PlanetHelper;
import com.machina.weather.system.ClientWeatherSystem;

import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Machina.MOD_ID, value = Dist.CLIENT)
public class ClientWeatherManager {
	public static ClientWeatherSystem WEATHER;

	@SubscribeEvent
	public static void onLoad(final EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide()) {
			return;
		}
		
		if (PlanetHelper.isPlanetLevel(event.getLevel().dimension())) {
			WEATHER = new ClientWeatherSystem((ClientLevel) event.getLevel());
		} else {
			WEATHER = null;
		}
	}
}
