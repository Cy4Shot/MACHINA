package com.machina.rocket;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DimensionSerializer implements EntityDataSerializer.ForValueType<ResourceKey<Level>> {
    
    public static final DimensionSerializer SERIALIZER = new DimensionSerializer();

    @Override
    public void write(FriendlyByteBuf buf, @NotNull ResourceKey<Level> key) {
        buf.writeResourceKey(key);
    }

    @Override
    public @NotNull ResourceKey<Level> read(FriendlyByteBuf buf) {
        return buf.readResourceKey(Registries.DIMENSION);
    }
}
