package com.machina.api.network.config;

import com.machina.api.network.ConfigMessage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SAckPayload(ConfigurationTask.Type innerType) implements ConfigMessage<C2SAckPayload> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SAckPayload> streamCodec() {
		return ByteBufCodecs.STRING_UTF8.map(ConfigurationTask.Type::new, ConfigurationTask.Type::toString)
				.map(C2SAckPayload::new, C2SAckPayload::innerType);
	}

	@Override
	public void handle(IPayloadContext context) {
		context.finishCurrentTask(innerType);
	}
}