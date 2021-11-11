package com.cy4.machina.tile_entity;

import com.cy4.machina.api.tile_entity.FluidTileEntity;
import com.cy4.machina.api.tile_entity.ITickableTile;
import com.cy4.machina.init.TileEntityTypesInit;

import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class PumpTileEntity extends FluidTileEntity implements ITickableTile {
	
	public PumpTileEntity() {
		super(TileEntityTypesInit.PUMP_TILE_ENTITY_TYPE);
	}

	@Override
	public World getWorld() { return this.level; }

	@Override
	protected FluidTank createTank() {
		return new FluidTank(FluidAttributes.BUCKET_VOLUME * 20);
	}

	@Override
	public void serverTick() {
		if (!this.level.getFluidState(worldPosition.below()).isEmpty()) {
			FluidState fluidState = this.level.getFluidState(worldPosition.below());
			FluidStack fluidStack = new FluidStack(fluidState.getType(), FluidAttributes.BUCKET_VOLUME);
			if (tank.isFluidValid(fluidStack) && tank.fill(fluidStack, FluidAction.SIMULATE) >= fluidStack.getAmount()) {
				tank.fill(fluidStack, FluidAction.EXECUTE);
				this.level.setBlock(worldPosition.below(), Blocks.AIR.defaultBlockState(), 3);
			}
		}
	}

}
