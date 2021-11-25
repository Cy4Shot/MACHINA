/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.network.message;

import com.cy4.machina.api.client.ClientStarchartHolder;
import com.cy4.machina.api.network.message.IMachinaMessage;
import com.cy4.machina.starchart.Starchart;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CSyncStarchart implements IMachinaMessage {

	private Starchart sc;

	public S2CSyncStarchart(Starchart starchart) {
		sc = starchart;
	}

	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(this, context)));
	}

	@OnlyIn(Dist.CLIENT)
	private void handleClient(S2CSyncStarchart msg, NetworkEvent.Context ctx) {
		ClientStarchartHolder.setStarchart(msg.sc);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeNbt(sc.serializeNBT());
	}

	public static S2CSyncStarchart decode(PacketBuffer buffer) {
		return new S2CSyncStarchart(new Starchart(buffer.readNbt()));
	}
}
