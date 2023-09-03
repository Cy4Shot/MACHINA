package com.machina.network;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkMessage {
	public void encode(FriendlyByteBuf buf);
	
	public void handle();
}
