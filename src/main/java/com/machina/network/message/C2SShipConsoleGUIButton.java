package com.machina.network.message;

import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SShipConsoleGUIButton implements INetworkMessage {

	public final BlockPos pos;
	public final int stage;

	public C2SShipConsoleGUIButton(BlockPos pos, int stage) {
		this.pos = pos;
		this.stage = stage;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity))
			return;
		ShipConsoleTileEntity te = (ShipConsoleTileEntity) e;
		te.stage = stage;
		te.updateStage();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(stage);
	}

	public static C2SShipConsoleGUIButton decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		int stage = buffer.readInt();
		return new C2SShipConsoleGUIButton(pos, stage);
	}

}
