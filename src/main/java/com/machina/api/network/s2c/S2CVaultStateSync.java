package com.machina.api.network.s2c;

import com.machina.api.network.S2CMessage;
import com.machina.client.ClientVaultState;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record S2CVaultStateSync(boolean inVault) implements S2CMessage<S2CVaultStateSync> {

	public static final StreamCodec<RegistryFriendlyByteBuf, S2CVaultStateSync> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, S2CVaultStateSync::inVault,
		S2CVaultStateSync::new
	);

	@Override
	public void handle(Player player) {
		ClientVaultState.setInVault(inVault);
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, S2CVaultStateSync> streamCodec() {
		return STREAM_CODEC;
	}
}
