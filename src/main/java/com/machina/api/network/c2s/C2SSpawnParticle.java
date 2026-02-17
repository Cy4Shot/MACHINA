package com.machina.api.network.c2s;

import org.joml.Vector3f;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.ParticleHelper;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public record C2SSpawnParticle<T extends ParticleOptions>(T options, float maxSpeed, int count, Vector3f pos, Vector3f offset)
        implements C2SMessage<C2SSpawnParticle<T>> {
    
    public C2SSpawnParticle(T options, float maxSpeed, int count, Vec3 pos, Vec3 offset) {
        this(options, maxSpeed, count, pos.toVector3f(), offset.toVector3f());
    }

    @SuppressWarnings("unchecked")
    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, C2SSpawnParticle<T>> streamCodec() {
        return StreamCodec.composite(ParticleTypes.STREAM_CODEC, C2SSpawnParticle::options, ByteBufCodecs.FLOAT,
                C2SSpawnParticle::maxSpeed, ByteBufCodecs.INT, C2SSpawnParticle::count, ByteBufCodecs.VECTOR3F,
                C2SSpawnParticle::pos, ByteBufCodecs.VECTOR3F, C2SSpawnParticle::offset, C2SSpawnParticle::new);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            ParticleHelper.spawnParticle((ServerLevel) player.level(), options, pos, count, maxSpeed, offset);
        });
    }
}