package com.machina.network.c2s;

import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SSetShipDestination implements INetworkMessage {

	public final BlockPos pos;
	public final int destination;

	public C2SSetShipDestination(BlockPos pos, int destination) {
		this.pos = pos;
		this.destination = destination;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, ShipConsoleTileEntity.class, e -> e.destination = this.destination);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(destination);
	}

	public static C2SSetShipDestination decode(PacketBuffer buffer) {
		return new C2SSetShipDestination(buffer.readBlockPos(), buffer.readInt());
	}

}
