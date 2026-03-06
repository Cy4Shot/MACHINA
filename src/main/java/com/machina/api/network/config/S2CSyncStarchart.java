package com.machina.api.network.config;

import com.machina.api.client.ClientStarchart;
import com.machina.api.network.ConfigMessage;
import com.machina.network.MachinaConfigurationTasks.MachinaStarchartConfigurationTask;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record S2CSyncStarchart(long seed) implements ConfigMessage<S2CSyncStarchart> {
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CSyncStarchart> streamCodec() {
		return ByteBufCodecs.VAR_LONG.map(S2CSyncStarchart::new, S2CSyncStarchart::seed).cast();
	}

	@Override
	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			ClientStarchart.sync(seed);
		}).exceptionally(e -> {
			context.disconnect(Component.literal("Starchart Sync Failed: " + e.getMessage()));
			return null;
		}).thenAccept(v -> {
			context.reply(new C2SAckPayload(MachinaStarchartConfigurationTask.TYPE));
		});
	}
}