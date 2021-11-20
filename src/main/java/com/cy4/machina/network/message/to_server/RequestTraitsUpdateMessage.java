package com.cy4.machina.network.message.to_server;

import com.cy4.machina.api.capability.planet_data.CapabilityPlanetData;
import com.cy4.machina.api.network.message.IMachinaMessage;

import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class RequestTraitsUpdateMessage implements IMachinaMessage {

	@Override
	public void handle(Context context) {
		CapabilityPlanetData.syncCapabilityWithClients(context.getSender().level);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		// Nothing to encode. Just requesting an update
	}

	public static RequestTraitsUpdateMessage decode(PacketBuffer buffer) {
		return new RequestTraitsUpdateMessage();
	}

}
