package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.base.PuzzleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SCompletePuzzle implements INetworkMessage {

	public final BlockPos pos;

	public C2SCompletePuzzle(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof PuzzleTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}
		((PuzzleTileEntity) e).completed();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SCompletePuzzle decode(PacketBuffer buffer) {
		return new C2SCompletePuzzle(buffer.readBlockPos());
	}

}