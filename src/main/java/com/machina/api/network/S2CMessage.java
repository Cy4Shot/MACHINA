package com.machina.api.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public interface S2CMessage {

	static Minecraft mc = Minecraft.getInstance();

	void encode(FriendlyByteBuf buf);

	void handle();
}
