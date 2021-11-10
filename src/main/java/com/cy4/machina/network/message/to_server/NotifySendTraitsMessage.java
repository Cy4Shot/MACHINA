package com.cy4.machina.network.message.to_server;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.network.message.IMachinaMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public class NotifySendTraitsMessage implements IMachinaMessage {

	public final ClientWorld clientWorld;

	public NotifySendTraitsMessage(ClientWorld clientWorld) {
		this.clientWorld = clientWorld;
	}

	@Override
	public void handle(Context context) {
		context.getSender().level.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY).ifPresent(cap -> {
			cap.getTraits().forEach(trait -> clientWorld.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY)
					.ifPresent(clientCap -> clientCap.addTrait(trait)));
		});
	}

	@Override
	public void encode(PacketBuffer buffer) {
		// Nothing to encode
	}

	@SuppressWarnings("resource")
	public static NotifySendTraitsMessage decode(PacketBuffer buffer) {
		return new NotifySendTraitsMessage(Minecraft.getInstance().level);
	}

}
