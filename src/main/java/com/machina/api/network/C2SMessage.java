package com.machina.api.network;

import com.machina.api.util.MachinaRL;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface C2SMessage<T extends C2SMessage<T>> extends CustomPacketPayload {
    void handle(MinecraftServer server, ServerPlayer player);

    public static <T extends C2SMessage<T>> Type<T> getType(Class<T> clazz) {
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
