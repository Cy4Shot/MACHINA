package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.fluid.PipeFluidStorage;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidPipeBlockEntity extends ConnectorBlockEntity<FluidStack, PipeFluidStorage> {

	public FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public FluidPipeBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.FLUID_PIPE.get(), pos, state);
	}

	@Override
	public int getRate() {
		return CommonConfig.pipeTransferRate.get();
	}

	@Override
	public PipeFluidStorage createStorage(Direction side) {
		return new PipeFluidStorage(this, side);
	}

	@Override
	public int slotsPerSide() {
		return 1;
	}
}
