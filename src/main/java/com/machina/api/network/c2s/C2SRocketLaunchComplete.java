package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record C2SRocketLaunchComplete(int entity) implements C2SMessage<C2SRocketLaunchComplete> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SRocketLaunchComplete> streamCodec() {
        return ByteBufCodecs.INT.map(C2SRocketLaunchComplete::new, C2SRocketLaunchComplete::entity).cast();
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level().getEntity(entity);
            if (e instanceof RocketEntity rocket) {
                rocket.completeLaunch(player);
            }
        });
    }
}