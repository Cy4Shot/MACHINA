package com.machina.network.c2s;

import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity)) {
			System.out.println("[ERROR] TE IS A NULL AAAAAAAAAAA");
		}
		((ShipConsoleTileEntity) e).launchAnim(tick);
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