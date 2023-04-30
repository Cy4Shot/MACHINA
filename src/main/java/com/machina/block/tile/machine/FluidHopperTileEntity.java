package com.machina.block.tile.machine;

import com.machina.block.FluidHopperBlock;
import com.machina.block.tile.BaseTileEntity;
import com.machina.capability.fluid.MachinaTank;
import com.machina.registration.init.TileEntityInit;
import com.machina.util.helper.FluidHelper;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;

public class FluidHopperTileEntity extends BaseTileEntity implements ITickableTileEntity {

	public FluidHopperTileEntity() {
		super(TileEntityInit.FLUID_HOPPER.get());
	}

	@Override
	public void tick() {
		int rate = 100;

		MachinaTank med = new MachinaTank(this, Integer.MAX_VALUE, p -> true, true, 0);
		Direction facing = level.getBlockState(worldPosition).getValue(FluidHopperBlock.FACING);
		if (FluidHelper.pullFluidsFromTank(facing, worldPosition, level, med, rate)) {
			if (!FluidHelper.pushFluidsToTank(facing, worldPosition, level, med, rate)) {
				FluidHelper.pushFluidsToTank(facing.getOpposite(), worldPosition, level, med, rate);
			}
		}
	}
}
