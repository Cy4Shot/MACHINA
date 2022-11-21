package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SCalculateRocketFuel implements INetworkMessage {

	public final BlockPos pos;

	public C2SCalculateRocketFuel(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}
		((ShipConsoleTileEntity) e).calculateFuel();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SCalculateRocketFuel decode(PacketBuffer buffer) {
		return new C2SCalculateRocketFuel(buffer.readBlockPos());
	}

}
