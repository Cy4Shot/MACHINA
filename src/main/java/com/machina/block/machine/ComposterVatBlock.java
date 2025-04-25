package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.ComposterVatBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class ComposterVatBlock extends LitMachineBlock {

	public ComposterVatBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.COMPOSTER_VAT.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return ComposterVatBlockEntity.class;
	}
}