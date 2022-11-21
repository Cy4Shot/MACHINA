package com.machina.network.c2s;

import com.machina.Machina;
import com.machina.block.tile.base.BaseEnergyTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SUpdateEnergySide implements INetworkMessage {

	public final BlockPos pos;
	public final int[] data;

	public C2SUpdateEnergySide(BlockPos pos, int[] data) {
		this.pos = pos;
		this.data = data;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof BaseEnergyTileEntity)) {
			Machina.LOGGER.error("TE IS A NULL AAAAAAAAAAA");
		}
		BaseEnergyTileEntity et = (BaseEnergyTileEntity) e;
		et.sides = data;
		et.sync();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeVarIntArray(data);
	}

	public static C2SUpdateEnergySide decode(PacketBuffer buffer) {
		return new C2SUpdateEnergySide(buffer.readBlockPos(), buffer.readVarIntArray());
	}

}
