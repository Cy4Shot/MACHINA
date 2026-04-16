package com.machina.api.cap.fluid;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class MirroredTank<T extends BlockEntity & MirroredTank.BlockEntityMirrorable> extends MachinaTank<T> {

	public static interface BlockEntityMirrorable {
		public FluidTank getMirrorableTank();
	}

	public MirroredTank(T tile) {
		super(tile, 1, stack -> true, 0, () -> {
		});
	}

	@Override
	public FluidStack getFluid() {
		FluidTank tank = tile.getMirrorableTank();
		return tank == null ? FluidStack.EMPTY : tank.getFluid();
	}

	@Override
	public int getCapacity() {
		FluidTank tank = tile.getMirrorableTank();
		return tank == null ? 0 : tank.getCapacity();
	}

	@Override
	public boolean isFluidValid(FluidStack stack) {
		FluidTank tank = tile.getMirrorableTank();
		return tank != null && tank.isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		FluidTank tank = tile.getMirrorableTank();
		return tank == null ? 0 : tank.fill(resource, action);
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		FluidTank tank = tile.getMirrorableTank();
		return tank == null ? FluidStack.EMPTY : tank.drain(maxDrain, action);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		FluidTank tank = tile.getMirrorableTank();
		return tank == null ? FluidStack.EMPTY : tank.drain(resource, action);
	}

}
