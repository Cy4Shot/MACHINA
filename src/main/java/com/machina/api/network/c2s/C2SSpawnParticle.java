package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.api.util.ParticleHelper;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public record C2SSpawnParticle<T extends ParticleOptions>(T options, float maxSpeed, int count, Vec3 pos, Vec3 offset)
        implements C2SMessage {

    @SuppressWarnings("unchecked")
    public static <X extends ParticleOptions> C2SSpawnParticle<X> decode(FriendlyByteBuf buf) {
        Vec3 pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 off = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        float speed = buf.readFloat();
        int count = buf.readInt();
        ParticleType<X> type = (ParticleType<X>) BuiltInRegistries.PARTICLE_TYPE.byId(buf.readInt());
        X opts = type.getDeserializer().fromNetwork(type, buf);
        return new C2SSpawnParticle<>(opts, speed, count, pos, off);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(pos.x());
        buf.writeDouble(pos.y());
        buf.writeDouble(pos.z());
        buf.writeDouble(offset.x());
        buf.writeDouble(offset.y());
        buf.writeDouble(offset.z());
        buf.writeFloat(this.maxSpeed);
        buf.writeInt(this.count);
        buf.writeInt(BuiltInRegistries.PARTICLE_TYPE.getId(options.getType()));
        options.writeToNetwork(buf);
    }

    public void handle(MinecraftServer server, ServerPlayer player) {
        server.execute(() -> {
            ParticleHelper.spawnParticle((ServerLevel) player.level(), options, pos, count, maxSpeed, offset);
        });
    }
}