package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record C2SRocketSetDestination(int entity, ResourceKey<Level> dim) implements C2SMessage {
    public static C2SRocketSetDestination decode(FriendlyByteBuf buf) {
        return new C2SRocketSetDestination(buf.readInt(), buf.readResourceKey(Registries.DIMENSION));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
        buf.writeResourceKey(dim);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level().getEntity(entity);
            if (e instanceof RocketEntity rocket) {
                rocket.setDestination(dim);
            }
        });
    }
}