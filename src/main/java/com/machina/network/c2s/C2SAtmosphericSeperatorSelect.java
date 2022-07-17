package com.machina.network.c2s;

import com.machina.block.tile.AtmosphericSeperatorTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SAtmosphericSeperatorSelect implements INetworkMessage {

	public final BlockPos pos;
	public final int id;

	public C2SAtmosphericSeperatorSelect(BlockPos pos, int id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public void handle(Context context) {
		TileEntity e = context.getSender().getLevel().getBlockEntity(this.pos);
		if (e == null || !(e instanceof AtmosphericSeperatorTileEntity)) {
			System.out.println("[ERROR] TE IS A NULL AAAAAAAAAAA");
		}

		((AtmosphericSeperatorTileEntity) e).setId(id);
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(id);
	}

	public static C2SAtmosphericSeperatorSelect decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		int id = buffer.readInt();
		return new C2SAtmosphericSeperatorSelect(pos, id);
	}

}
