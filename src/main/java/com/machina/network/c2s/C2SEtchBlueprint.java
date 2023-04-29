package com.machina.network.c2s;

import com.machina.block.tile.machine.BlueprinterTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SEtchBlueprint implements INetworkMessage {

	public final BlockPos pos;
	public final String id;

	public C2SEtchBlueprint(BlockPos pos, String id) {
			this.pos = pos;
			this.id = id;
		}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, BlueprinterTileEntity.class, e -> e.etch(id));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeUtf(id);
	}

	public static C2SEtchBlueprint decode(PacketBuffer buffer) {
		return new C2SEtchBlueprint(buffer.readBlockPos(), buffer.readUtf());
	}

}
