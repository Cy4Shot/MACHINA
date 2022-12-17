package com.machina.network.s2c;

import com.machina.client.ClientResearch;
import com.machina.network.INetworkMessage;
import com.machina.research.ResearchTree;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2CResearchSync implements INetworkMessage {
	private final ResearchTree research;

	public S2CResearchSync(final ResearchTree research) {
		this.research = research;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			ClientResearch.setResearch(research);
		});
		context.setPacketHandled(true);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeNbt(research.serializeNBT());
	}

	public static S2CResearchSync decode(PacketBuffer buffer) {
		return new S2CResearchSync(ResearchTree.fromNBT(buffer.readNbt()));
	}
}
