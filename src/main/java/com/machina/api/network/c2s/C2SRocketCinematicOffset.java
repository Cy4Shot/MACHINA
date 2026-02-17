package com.machina.api.network.c2s;

import org.joml.Vector3f;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record C2SRocketCinematicOffset(int entity, Vector3f pos, double off)
        implements C2SMessage<C2SRocketCinematicOffset> {

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SRocketCinematicOffset> streamCodec() {
        return StreamCodec.composite(ByteBufCodecs.INT, C2SRocketCinematicOffset::entity, ByteBufCodecs.VECTOR3F,
                C2SRocketCinematicOffset::pos, ByteBufCodecs.DOUBLE, C2SRocketCinematicOffset::off,
                C2SRocketCinematicOffset::new);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            Entity e = player.level().getEntity(entity);
            if (e instanceof RocketEntity rocket) {
                rocket.moveTo(new Vec3(pos).add(0, off, 0));
            }
        });
    }
}