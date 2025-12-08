package com.machina.rocket;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionSerializer implements EntityDataSerializer.ForValueType<ResourceKey<Level>> {
    
    public static final DimensionSerializer SERIALIZER = new DimensionSerializer();

    @Override
    public void write(FriendlyByteBuf buf, ResourceKey<Level> key) {
        buf.writeResourceKey(key);
    }

    @Override
    public ResourceKey<Level> read(FriendlyByteBuf buf) {
        return buf.readResourceKey(Registries.DIMENSION);
    }
}
