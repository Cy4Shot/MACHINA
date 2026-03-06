package com.machina.api.network;

import com.machina.api.util.MachinaRL;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ConfigMessage<T extends ConfigMessage<T>> extends CustomPacketPayload {

	void handle(IPayloadContext context);

	public static <T extends ConfigMessage<T>> Type<T> getType(Class<T> clazz) {
		String id = clazz.getSimpleName().toLowerCase();
		return new CustomPacketPayload.Type<>(MachinaRL.create(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	default Type<? extends CustomPacketPayload> type() {
		return getType(getClass());
	}

	StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
}
