package com.machina.network.c2s;

import com.machina.block.tile.machine.FabricatorTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.helper.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SFabricatorFabricate implements INetworkMessage {

	public final BlockPos pos;

	public C2SFabricatorFabricate(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, FabricatorTileEntity.class, e -> e.fabricate());
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SFabricatorFabricate decode(PacketBuffer buffer) {
		return new C2SFabricatorFabricate(buffer.readBlockPos());
	}
}