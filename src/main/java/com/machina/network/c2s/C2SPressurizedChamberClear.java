package com.machina.network.c2s;

import com.machina.block.tile.machine.PressurizedChamberTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SPressurizedChamberClear implements INetworkMessage {

	public final BlockPos pos;
	public final int id;

	public C2SPressurizedChamberClear(BlockPos pos, int id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, PressurizedChamberTileEntity.class, e -> e.clear(id));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(id);
	}

	public static C2SPressurizedChamberClear decode(PacketBuffer buffer) {
		return new C2SPressurizedChamberClear(buffer.readBlockPos(), buffer.readInt());
	}

}
