package com.machina.block;

import com.machina.api.block.MultiblockBlock;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestMBlock extends MultiblockBlock {

	public TestMBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getTE() {
		return BlockEntityInit.TEST_MASTER.get();
	}

	@Override
	public boolean isMaster() {
		return true;
	}
}