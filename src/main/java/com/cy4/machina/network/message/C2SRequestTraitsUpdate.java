package com.cy4.machina.network.message;

import com.cy4.machina.api.network.message.IMachinaMessage;
import com.cy4.machina.api.util.helper.StarchartHelper;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SRequestTraitsUpdate implements IMachinaMessage {

	@Override
	public void handle(Context context) {
		StarchartHelper.syncCapabilityWithClients(context.getSender().level);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		// Nothing to encode. Just requesting an update
	}

	public static C2SRequestTraitsUpdate decode(PacketBuffer buffer) {
		return new C2SRequestTraitsUpdate();
	}

}
