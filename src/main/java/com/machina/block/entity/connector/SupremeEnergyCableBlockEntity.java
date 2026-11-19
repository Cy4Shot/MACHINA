package com.machina.block.entity.connector;

import com.machina.api.block.entity.ConnectorBlockEntity;
import com.machina.api.cap.energy.CableEnergyStorage;
import com.machina.config.CommonConfig;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SupremeEnergyCableBlockEntity extends ConnectorBlockEntity<Integer, CableEnergyStorage> {

	public SupremeEnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public SupremeEnergyCableBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityInit.SUPREME_ENERGY_CABLE.get(), pos, state);
	}

	@Override
	public int getRate() {
		return CommonConfig.supremeCableTransferRate.get();
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
