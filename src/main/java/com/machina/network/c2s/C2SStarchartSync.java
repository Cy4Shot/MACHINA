package com.machina.network.c2s;

import java.util.UUID;

import com.machina.network.INetworkMessage;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CStarchartSync;
import com.machina.util.server.ServerHelper;
import com.machina.world.data.StarchartData;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

public class C2SStarchartSync implements INetworkMessage {

	UUID id;

	public C2SStarchartSync(UUID uuid) {
		this.id = uuid;
	}

	@Override
	public void handle(Context context) {
		MachinaNetwork.CHANNEL.send(
				PacketDistributor.PLAYER.with(() -> ServerHelper.server().getPlayerList().getPlayer(id)),
				new S2CStarchartSync(StarchartData.getStarchartForServer(ServerHelper.server())));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeUUID(id);
	}

	public static C2SStarchartSync decode(PacketBuffer buffer) {
		return new C2SStarchartSync(buffer.readUUID());
	}

}