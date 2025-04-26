package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.SolidifierBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class SolidifierBlock extends LitMachineBlock {

	public SolidifierBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.SOLIDIFIER.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return SolidifierBlockEntity.class;
	}
}