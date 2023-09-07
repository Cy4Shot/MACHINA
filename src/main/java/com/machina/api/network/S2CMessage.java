package com.machina.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface S2CMessage {
	void encode(FriendlyByteBuf buf);
	
	void handle();
}
