package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.BlueprinterTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
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
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof BlueprinterTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}

		((BlueprinterTileEntity) e).etch(id);
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
