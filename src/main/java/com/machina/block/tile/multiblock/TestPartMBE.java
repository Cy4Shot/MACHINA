package com.machina.block.tile.multiblock;

import com.machina.api.block.tile.multiblock.MultiblockPartBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestPartMBE extends MultiblockPartBlockEntity {

	public TestPartMBE(BlockPos pos, BlockState state) {
		this(BlockEntityInit.TEST_PART.get(), pos, state);
	}
	
	public TestPartMBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.TEST;
	}

	@Override
	public boolean isPort() {
		return false;
	}
}