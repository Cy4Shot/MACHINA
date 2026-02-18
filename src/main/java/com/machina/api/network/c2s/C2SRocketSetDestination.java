package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;
import com.machina.rocket.RocketEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record C2SRocketSetDestination(int entity, ResourceKey<Level> dim)
		implements C2SMessage<C2SRocketSetDestination> {
	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SRocketSetDestination> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.INT, C2SRocketSetDestination::entity,
				ResourceKey.streamCodec(Registries.DIMENSION), C2SRocketSetDestination::dim,
				C2SRocketSetDestination::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			Entity e = player.level().getEntity(entity);
			if (e instanceof RocketEntity rocket) {
				rocket.setDestination(dim);
			}
		});
	}
}