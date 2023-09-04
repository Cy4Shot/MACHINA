package com.machina.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkMessage {
	void encode(FriendlyByteBuf buf);
	
	void handle();
}
