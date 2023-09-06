package com.machina.block;

import com.machina.api.block.MultiblockBlock;
import com.machina.registration.init.BlockEntityInit;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class TestPBlock extends MultiblockBlock {

	public TestPBlock(Properties props) {
		super(props);
	}

	@Override
	public BlockEntityType<?> getTE() {
		return BlockEntityInit.TEST_PART.get();
	}

	@Override
	public boolean isMaster() {
		return false;
	}
}