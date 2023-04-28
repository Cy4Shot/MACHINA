package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.base.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}
		
		((ShipConsoleTileEntity) e).destination = this.destination;
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
