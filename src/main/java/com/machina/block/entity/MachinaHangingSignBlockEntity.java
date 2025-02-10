package com.machina.block.entity;

import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MachinaHangingSignBlockEntity extends HangingSignBlockEntity {

	public MachinaHangingSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public @NotNull BlockEntityType<?> getType() {
		return BlockEntityInit.HANGING_SIGN.get();
	}
}