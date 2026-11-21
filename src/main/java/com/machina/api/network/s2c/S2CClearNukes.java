package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.client.explosion.NuclearExplosionManager;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CClearNukes() implements S2CMessage<S2CClearNukes> {

	public static final StreamCodec<RegistryFriendlyByteBuf, S2CClearNukes> CODEC =
		StreamCodec.unit(new S2CClearNukes());

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CClearNukes> streamCodec() {
		return CODEC;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		NuclearExplosionManager.clear();
	}
}
