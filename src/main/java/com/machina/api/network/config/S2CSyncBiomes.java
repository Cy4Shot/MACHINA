package com.machina.api.network.config;

import java.util.HashMap;

import com.machina.api.client.ClientBiomeSettings;
import com.machina.api.network.ConfigMessage;
import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;
import com.machina.network.MachinaConfigurationTasks.MachinaBiomeConfigurationTask;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record S2CSyncBiomes(HashMap<ResourceLocation, PlanetBiomeClientSettings> biomeSettings)
		implements ConfigMessage<S2CSyncBiomes> {
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CSyncBiomes> streamCodec() {
		return ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, PlanetBiomeClientSettings.STREAM_CODEC)
				.map(S2CSyncBiomes::new, S2CSyncBiomes::biomeSettings);
	}

	@Override
	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			ClientBiomeSettings.BIOME_SETTINGS = biomeSettings();
		}).exceptionally(e -> {
			context.disconnect(Component.literal("Biome Sync Failed: " + e.getMessage()));
			return null;
		}).thenAccept(v -> {
			context.reply(new C2SAckPayload(MachinaBiomeConfigurationTask.TYPE));
		});
	}
}