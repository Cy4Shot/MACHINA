package com.machina.network.c2s;

import com.machina.block.tile.ShipConsoleTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SShipConsoleGUIButton implements INetworkMessage {

	public final BlockPos pos;

	public C2SShipConsoleGUIButton(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void handle(Context context) {
		ServerWorld world = context.getSender().getLevel();
		TileEntity e = world.getBlockEntity(this.pos);
		if (e == null || !(e instanceof ShipConsoleTileEntity)) {
			System.out.println("[ERROR] TE IS A NULL AAAAAAAAAAA");
		}
		((ShipConsoleTileEntity) e).buttonPressed();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
	}

	public static C2SShipConsoleGUIButton decode(PacketBuffer buffer) {
		BlockPos pos = buffer.readBlockPos();
		return new C2SShipConsoleGUIButton(pos);
	}

}