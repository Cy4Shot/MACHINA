package com.machina.network.s2c;

import com.machina.client.cinema.CinematicHandler;
import com.machina.client.cinema.cinematics.LaunchCinematic;
import com.machina.network.INetworkMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CLaunchShip implements INetworkMessage {

	private BlockPos pos;

	public S2CLaunchShip(BlockPos p) {
		this.pos = p;
	}

	@SuppressWarnings("resource")
	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> {
			CinematicHandler.INSTANCE.setCinematic(new LaunchCinematic(Minecraft.getInstance().player, pos));
		});
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static S2CLaunchShip decode(PacketBuffer buffer) {
		return new S2CLaunchShip(buffer.readBlockPos());
	}
}