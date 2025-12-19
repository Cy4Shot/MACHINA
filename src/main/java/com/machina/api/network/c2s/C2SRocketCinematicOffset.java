package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record C2SRocketCinematicOffset(int entity, Vec3 pos, double off) implements C2SMessage {
    public static C2SRocketCinematicOffset decode(FriendlyByteBuf buf) {
        int id = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double o = buf.readDouble();
        return new C2SRocketCinematicOffset(id, new Vec3(x, y, z), o);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entity);
        buf.writeDouble(pos.x);
        buf.writeDouble(pos.y);
        buf.writeDouble(pos.z);
        buf.writeDouble(off);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level().getEntity(entity);
            if (e instanceof RocketEntity rocket) {
                rocket.moveTo(pos.add(0, off, 0));
            }
        });
    }
}