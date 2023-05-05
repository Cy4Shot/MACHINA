package com.machina.network.c2s;

import com.machina.block.tile.basic.PuzzleTileEntity;
import com.machina.network.INetworkMessage;
import com.machina.util.helper.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SCompletePuzzle implements INetworkMessage {

	public final BlockPos pos;

	public C2SCompletePuzzle(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, PuzzleTileEntity.class, e -> e.completed());
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SCompletePuzzle decode(PacketBuffer buffer) {
		return new C2SCompletePuzzle(buffer.readBlockPos());
	}

}