package com.machina.network;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.machina.Machina;
import com.machina.api.network.config.S2CSyncBiomes;
import com.machina.api.network.config.S2CSyncStarchart;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.api.util.MachinaRL;

import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

@EventBusSubscriber(modid = Machina.MOD_ID)
public class MachinaConfigurationTasks {

	@SubscribeEvent
	public static void register(final RegisterConfigurationTasksEvent event) {
		event.register(new MachinaStarchartConfigurationTask(event.getListener()));
		event.register(new MachinaBiomeConfigurationTask(event.getListener()));
	}

	public static record MachinaStarchartConfigurationTask(ServerCommonPacketListener listener)
			implements ICustomConfigurationTask {
		public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type(
				MachinaRL.create("sync_starchart"));

		@Override
		public void run(final Consumer<CustomPacketPayload> sender) {
			if (listener instanceof ServerCommonPacketListenerImpl impl) {
				if (impl.getMainThreadEventLoop() instanceof MinecraftServer server) {
					long seed = server.overworld().getSeed();
					sender.accept(new S2CSyncStarchart(seed));
				}
			}
		}

		@Override
		public ConfigurationTask.Type type() {
			return TYPE;
		}
	}

	public static record MachinaBiomeConfigurationTask(ServerCommonPacketListener listener)
			implements ICustomConfigurationTask {
		public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type(MachinaRL.create("sync_biomes"));

		@Override
		public void run(final Consumer<CustomPacketPayload> sender) {
			HashMap<ResourceLocation, PlanetBiomeClientSettings> biomeSettings = new HashMap<>();
			for (Entry<ResourceLocation, PlanetBiomeSettings> entry : PlanetBiomeLoader.INSTANCE.getEntrySet()) {
				biomeSettings.put(entry.getKey(), entry.getValue().client_settings());
			}
			sender.accept(new S2CSyncBiomes(biomeSettings));
		}

		@Override
		public ConfigurationTask.Type type() {
			return TYPE;
		}
	}
}
