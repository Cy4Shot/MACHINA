package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.energy.CableEnergyStorage;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicEnergyCableBlockEntity extends ConnectorBlockEntity<Integer, CableEnergyStorage> {

	public BasicEnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BasicEnergyCableBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.BASIC_ENERGY_CABLE.get(), pos, state);
	}

	@Override
	public int getRate() {
		return CommonConfig.basicCableTransferRate.get();
	}

	@Override
	public CableEnergyStorage createStorage(Direction side) {
		return new CableEnergyStorage(this, side);
	}

	@Override
	public int slotsPerSide() {
		return 0;
	}
}
