package com.machina.network.c2s;

import com.machina.block.tile.machine.AtmosphericSeparatorTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.server.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SAtmosphericSeparatorSelect implements INetworkMessage {

	public final BlockPos pos;
	public final int id;

	public C2SAtmosphericSeparatorSelect(BlockPos pos, int id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, AtmosphericSeparatorTileEntity.class, e -> e.setId(id));	
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(id);
	}

	public static C2SAtmosphericSeparatorSelect decode(PacketBuffer buffer) {
		return new C2SAtmosphericSeparatorSelect(buffer.readBlockPos(), buffer.readInt());
	}

}
