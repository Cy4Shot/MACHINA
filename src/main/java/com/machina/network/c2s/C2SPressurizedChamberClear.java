package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.PressurizedChamberTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof PressurizedChamberTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}
		((PressurizedChamberTileEntity) e).clear(id);
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
