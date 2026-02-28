package com.machina.api.network.s2c;

import com.machina.api.client.cinema.CinematicHandler;
import com.machina.api.network.S2CMessage;
import com.machina.client.cinema.LandCinematic;
import com.machina.rocket.RocketEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CCinematicLand(int entity) implements S2CMessage<S2CCinematicLand> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CCinematicLand> streamCodec() {
		return ByteBufCodecs.INT.map(S2CCinematicLand::new, S2CCinematicLand::entity).cast();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		CinematicHandler.INSTANCE.enqueueCinematic(() -> player.level() != null, () -> {
			Entity e = player.level().getEntity(entity);
			if (e instanceof RocketEntity rocket) {
				return new LandCinematic(rocket);
			}
			return null;
		});
	}
}