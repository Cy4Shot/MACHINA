package com.machina.block.machine;

import com.machina.api.block.LitMachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.MixerBlockEntity;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class MixerBlock extends LitMachineBlock {

	public MixerBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.MIXER.get();
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return MixerBlockEntity.class;
	}
}