package com.machina.api.network.c2s;

import com.machina.api.event.CinematicCompleteEvent;
import com.machina.api.network.C2SMessage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;

public record C2SFinishCinematic(String id) implements C2SMessage<C2SFinishCinematic> {

    @Override
    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> NeoForge.EVENT_BUS.post(new CinematicCompleteEvent(player, id)));
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SFinishCinematic> streamCodec() {
        return ByteBufCodecs.STRING_UTF8.map(C2SFinishCinematic::new, C2SFinishCinematic::id).cast();
    }
}