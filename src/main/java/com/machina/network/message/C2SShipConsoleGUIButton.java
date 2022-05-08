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
	public final int s;

	public C2SShipConsoleGUIButton(BlockPos pos, int stage) {
		this.pos = pos;
		this.s = stage;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity)) {
			System.out.println("[ERROR] TE IS A NULL AAAAAAAAAAA");
		}
		ShipConsoleTileEntity te = (ShipConsoleTileEntity) e;
		if(te.stage == 1) te.stage = 2;
		else if(te.stage == 2) te.stage = 1;
		System.out.println(te.stage);
//		te.stage = s;
		te.updateStage();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(s);
	}

	public static C2SShipConsoleGUIButton decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		int stage = buffer.readInt();
		return new C2SShipConsoleGUIButton(pos, stage);
	}

}
