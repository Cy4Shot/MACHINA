package com.machina.network.c2s;

import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SShipLaunchEffect implements INetworkMessage {

	public final BlockPos pos;
	public final int tick;

	public C2SShipLaunchEffect(BlockPos pos,int tick) {
		this.pos = pos;
		this.tick = tick;
	;}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, ShipConsoleTileEntity.class, e -> e.launchAnim(tick));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(tick);
	}

	public static C2SShipLaunchEffect decode(PacketBuffer buffer) {
		return new C2SShipLaunchEffect(buffer.readBlockPos(), buffer.readInt());
	}

}