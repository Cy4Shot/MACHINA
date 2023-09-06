package com.machina.block.tile.multiblock;

import com.machina.api.block.tile.multiblock.MultiblockMasterBlockEntity;
import com.machina.api.cap.item.MachinaItemStorage;
import com.machina.registration.init.BlockEntityInit;
import com.machina.registration.init.MultiblockInit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestMasterMBE extends MultiblockMasterBlockEntity {

	public TestMasterMBE(BlockPos pos, BlockState state) {
		this(BlockEntityInit.TEST_MASTER.get(), pos, state);
	}
	
	public TestMasterMBE(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public ResourceLocation getMultiblock() {
		return MultiblockInit.TEST;
	}

	MachinaItemStorage items;

	@Override
	public void createStorages() {
		this.items = add(new MachinaItemStorage(5));
	}
}
