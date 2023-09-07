package com.machina.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface C2SMessage {
	void encode(FriendlyByteBuf buf);
	
	void handle(MinecraftServer server, ServerPlayer player);
}
