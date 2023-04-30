package com.machina.network.c2s;

import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.helper.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SCalculateRocketFuel implements INetworkMessage {

	public final BlockPos pos;

	public C2SCalculateRocketFuel(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, ShipConsoleTileEntity.class, e -> e.calculateFuel());
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SCalculateRocketFuel decode(PacketBuffer buffer) {
		return new C2SCalculateRocketFuel(buffer.readBlockPos());
	}

}
