package com.machina.network.s2c;

import com.machina.client.screen.overlay.ResearchToastOverlay;
import com.machina.network.INetworkMessage;
import com.machina.registration.init.ResearchInit;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CResearchToast implements INetworkMessage {

	private String id;

	public S2CResearchToast(String i) {
		this.id = i;
	}

	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> {
			ResearchToastOverlay.playToast(ResearchInit.RESEARCHES.get(id));
		});
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeUtf(id);
	}

	public static S2CResearchToast decode(PacketBuffer buffer) {
		return new S2CResearchToast(buffer.readUtf());
	}
}