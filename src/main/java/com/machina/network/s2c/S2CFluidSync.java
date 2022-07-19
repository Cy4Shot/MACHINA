package com.machina.network.s2c;

import com.machina.block.tile.base.IFluidTileEntity;
import com.machina.network.INetworkMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CFluidSync implements INetworkMessage {

	private BlockPos pos;
	private FluidStack fluid;

	public S2CFluidSync(BlockPos p, FluidStack f) {
		this.pos = p;
		this.fluid = f;
	}

	@SuppressWarnings("resource")
	@Override
	public void handle(Context context) {
		context.enqueueWork(() -> {
			TileEntity te = Minecraft.getInstance().level.getBlockEntity(pos);
			if (te instanceof IFluidTileEntity) {
				((IFluidTileEntity) te).setFluid(fluid);
			}
		});
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeFluidStack(fluid);
	}

	public static S2CFluidSync decode(PacketBuffer buffer) {
		return new S2CFluidSync(buffer.readBlockPos(), buffer.readFluidStack());
	}

}
