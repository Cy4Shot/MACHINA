package com.machina.network.c2s;

import com.machina.block.tile.machine.PressurizedChamberTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SPressurizedChamberRunning implements INetworkMessage {

	public final BlockPos pos;

	public C2SPressurizedChamberRunning(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, PressurizedChamberTileEntity.class, e -> e.runToggle());
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SPressurizedChamberRunning decode(PacketBuffer buffer) {
		return new C2SPressurizedChamberRunning(buffer.readBlockPos());
	}

}
