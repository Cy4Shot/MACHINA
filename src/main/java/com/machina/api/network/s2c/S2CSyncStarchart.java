package com.machina.api.network.s2c;

import com.machina.api.client.ClientStarchart;
import com.machina.api.network.S2CMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public record S2CSyncStarchart(long seed) implements S2CMessage {
	
	public static S2CSyncStarchart decode(FriendlyByteBuf buf) {
		return new S2CSyncStarchart(buf.readLong());
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeLong(seed);
	}

	@Override
	public void handle() {
		long seed = seed();
		
		Minecraft mc = Minecraft.getInstance();
		mc.execute(new Runnable() {
			@Override
			public void run() {
				ClientStarchart.sync(seed);
			}
		});
	}
}