package com.cy4.machina.network.message.to_client;

import com.cy4.machina.api.capability.trait.CapabilityPlanetTrait;
import com.cy4.machina.api.capability.trait.DefaultPlanetTraitCapability;
import com.cy4.machina.api.capability.trait.IPlanetTraitCapability;
import com.cy4.machina.api.network.message.IMachinaMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SyncTraitsCapabilityMessage implements IMachinaMessage {

	private final IPlanetTraitCapability cap;

	public SyncTraitsCapabilityMessage(IPlanetTraitCapability cap) {
		this.cap = cap;
	}

	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(this, context)));
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	private void handleClient(SyncTraitsCapabilityMessage msg, NetworkEvent.Context ctx) {
		World world = Minecraft.getInstance().level;
		if (world != null) {
			world.getCapability(CapabilityPlanetTrait.PLANET_TRAIT_CAPABILITY)
			.ifPresent(clientCap -> msg.cap.getTraits().forEach(clientCap::addTrait));
		}
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeNbt(CapabilityPlanetTrait.Storage.serialize(cap));
	}

	public static SyncTraitsCapabilityMessage decode(PacketBuffer buffer) {
		IPlanetTraitCapability cap = new DefaultPlanetTraitCapability();
		CapabilityPlanetTrait.Storage.deserialize(buffer.readNbt(), cap);
		return new SyncTraitsCapabilityMessage(cap);
	}

}
