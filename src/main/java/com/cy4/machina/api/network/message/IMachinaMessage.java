/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.network.message;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent;

public interface IMachinaMessage {

	void handle(NetworkEvent.Context context);

	void encode(PacketBuffer buffer);

	static <MSG extends IMachinaMessage> void handle(MSG message, Supplier<NetworkEvent.Context> ctx) {
		if (message != null) {
			NetworkEvent.Context context = ctx.get();
			context.enqueueWork(() -> message.handle(context));
			context.setPacketHandled(true);
		}
	}

}
