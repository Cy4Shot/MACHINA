package com.machina.api.network.c2s;

import com.machina.api.network.C2SMessage;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SItemMenuSync(int slot, ItemStack stack) implements C2SMessage<C2SItemMenuSync> {

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, C2SItemMenuSync> streamCodec() {
		return StreamCodec.composite(ByteBufCodecs.INT, C2SItemMenuSync::slot, ItemStack.STREAM_CODEC,
				C2SItemMenuSync::stack, C2SItemMenuSync::new);
	}

	@Override
	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			player.getInventory().setItem(slot, stack);
			player.getInventory().setChanged();
		});
	}
}