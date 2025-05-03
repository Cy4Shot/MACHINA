package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.RocketPartBenchBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class RocketPartBenchBlock extends MachineBlock {

	public RocketPartBenchBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.ROCKET_PART_BENCH.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return RocketPartBenchBlockEntity.class;
	}
}