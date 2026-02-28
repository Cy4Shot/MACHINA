package com.machina.api.network.s2c;

import com.machina.api.client.ClientStarchart;
import com.machina.api.network.S2CMessage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record S2CSyncStarchart(long seed) implements S2CMessage<S2CSyncStarchart> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CSyncStarchart> streamCodec() {
		return ByteBufCodecs.VAR_LONG.map(S2CSyncStarchart::new, S2CSyncStarchart::seed).cast();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(Player player) {
		ClientStarchart.sync(seed);
	}
}