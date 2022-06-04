package com.machina.network.message;

import com.machina.block.tile.EnergyTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SEnergyDirChange implements INetworkMessage {

	public final BlockPos pos;
	public final int[] s;

	public C2SEnergyDirChange(BlockPos pos, int[] s) {
		this.pos = pos;
		this.s = s;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof EnergyTileEntity)) {
			System.out.println("[ERROR] TE IS A NULL AAAAAAAAAAA");
		}
		((EnergyTileEntity) e).sides = s;
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeVarIntArray(s);
	}

	public static C2SEnergyDirChange decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		int[] s = buffer.readVarIntArray();
		return new C2SEnergyDirChange(pos, s);
	}

}