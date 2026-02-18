package com.machina.rocket;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionSerializer implements EntityDataSerializer.ForValueType<ResourceKey<Level>> {

	public static final DimensionSerializer SERIALIZER = new DimensionSerializer();

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, ResourceKey<Level>> codec() {
		return ResourceKey.streamCodec(Registries.DIMENSION);
	}
}
