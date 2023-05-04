package com.machina.network.c2s;

import com.machina.block.tile.MachinaTileEntity;
import com.machina.capability.fluid.MachinaFluidStorage;
import com.machina.capability.fluid.MachinaTank;
import com.machina.network.INetworkMessage;
import com.machina.util.helper.BlockHelper;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class C2SClearTank implements INetworkMessage {

	public final BlockPos pos;
	public final int id;

	public C2SClearTank(BlockPos pos, int id) {
		this.pos = pos;
		this.id = id;
	}

	@Override
	public void handle(Context context) {
		BlockHelper.doWithTe(context, pos, MachinaTileEntity.class,
				e -> e.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).ifPresent(handler -> {
					MachinaTank tank = ((MachinaFluidStorage) handler).tank(id);
					tank.drainRaw(tank.getFluid(), FluidAction.EXECUTE);
				}));
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeInt(id);
	}

	public static C2SClearTank decode(PacketBuffer buffer) {
		return new C2SClearTank(buffer.readBlockPos(), buffer.readInt());
	}

}