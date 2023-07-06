package com.machina.weather.event;

import java.util.Map;

import com.machina.Machina;
import com.machina.util.helper.PlanetHelper;
import com.machina.weather.WeatherManagerServer;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeatherServerEvents {
	private static final Map<RegistryKey<World>, WeatherManagerServer> MANAGERS = new Reference2ObjectOpenHashMap<>();

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		IWorld world = event.getWorld();
		if (!world.isClientSide() && world instanceof ServerWorld) {
			ServerWorld sw = (ServerWorld) world;
			if (PlanetHelper.isDimensionPlanet(sw.dimension())) {
				MANAGERS.put(sw.dimension(), new WeatherManagerServer(sw));
			}
		}
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		IWorld world = event.getWorld();
		if (!world.isClientSide() && world instanceof ServerWorld) {
			ServerWorld sw = (ServerWorld) world;
			if (PlanetHelper.isDimensionPlanet(sw.dimension())) {
				MANAGERS.remove(sw.dimension());
			}
		}
	}

	@SubscribeEvent
	public static void tickServer(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			for (WeatherManagerServer manager : MANAGERS.values()) {
				manager.tick();
			}
		}
	}

	public static WeatherManagerServer getWeatherManagerFor(RegistryKey<World> dimension) {
		return MANAGERS.get(dimension);
	}
}
