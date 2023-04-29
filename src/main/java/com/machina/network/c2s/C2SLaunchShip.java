package com.machina.network.c2s;

import java.util.UUID;

import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SLaunchShip implements INetworkMessage {

	public final BlockPos pos;
	public final UUID id;

	public C2SLaunchShip(BlockPos pos, UUID id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, ShipConsoleTileEntity.class, e -> e.launch(id));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeUUID(id);
	}

	public static C2SLaunchShip decode(PacketBuffer buffer) {
		return new C2SLaunchShip(buffer.readBlockPos(), buffer.readUUID());
	}

}