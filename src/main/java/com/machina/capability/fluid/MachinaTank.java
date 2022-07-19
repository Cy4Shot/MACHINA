package com.machina.capability.fluid;

import java.util.function.Predicate;

import com.machina.block.tile.base.BaseTileEntity;
import com.machina.network.MachinaNetwork;
import com.machina.network.s2c.S2CFluidSync;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MachinaTank extends FluidTank {

	private BaseTileEntity tile;

	public MachinaTank(BaseTileEntity tile, int capacity, Predicate<FluidStack> validator) {
		super(capacity, validator);
		this.tile = tile;
	}

	@Override
	public void onContentsChanged() {
		IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).orElse(null);
		if (handler == null || handler.getFluidInTank(0) == null) {
			return;
		}
		FluidStack f = handler.getFluidInTank(0);
		if (!tile.getLevel().isClientSide()) {
			MachinaNetwork.sendToClients(MachinaNetwork.CHANNEL, new S2CFluidSync(tile.getBlockPos(), f));
		}
	}
}