package com.machina.api.network;

import com.machina.api.util.MachinaRL;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface S2CMessage<T extends S2CMessage<T>> extends CustomPacketPayload {

	void handle(Player player);

	public static <T extends S2CMessage<T>> Type<T> getType(Class<T> clazz) {
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
