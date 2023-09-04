package com.machina.block.tile.machine;

import com.machina.api.block.tile.MachinaBlockEntity;
import com.machina.api.cap.energy.IEnergyBlockEntity;
import com.machina.api.cap.energy.MachinaEnergyStorage;
import com.machina.api.cap.fluid.MachinaFluidStorage;
import com.machina.api.cap.fluid.MachinaTank;
import com.machina.api.cap.item.MachinaItemStorage;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestBE extends MachinaBlockEntity implements IEnergyBlockEntity {

	public TestBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	public TestBE(BlockPos pos, BlockState state) {
		this(BlockEntityInit.TEST.get(), pos, state);
	}

	MachinaItemStorage items;
	MachinaEnergyStorage energy;
	MachinaFluidStorage fluids;

	@Override
	public void createStorages() {
		this.energy = add(new MachinaEnergyStorage(this, 100000, 1000));
		this.fluids = add(new MachinaTank(this, 10000, f -> true, false, 0),
				new MachinaTank(this, 10000, f -> false, true, 1));
		this.items = add(new MachinaItemStorage(3));
	}

	@Override
	public boolean isGenerator() {
		return false;
	}
}