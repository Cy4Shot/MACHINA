package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record C2SRocketLaunchComplete(int entity) implements C2SMessage {
    public static C2SRocketLaunchComplete decode(FriendlyByteBuf buf) {
        return new C2SRocketLaunchComplete(buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level().getEntity(entity);
            if (e instanceof RocketEntity rocket) {
                rocket.completeLaunch(player);
            }
        });
    }
}