package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.RocketAssemblyStationBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class RocketAssemblyStationBlock extends MachineBlock {

	public RocketAssemblyStationBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.ROCKET_ASSEMBLY_STATION.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return RocketAssemblyStationBlockEntity.class;
	}
	
	@Override
	protected boolean isTickable() {
		return true;
	}
}