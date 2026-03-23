package com.machina.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalBlock extends Block {

	public CrystalBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState p_60525_, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
	}
}
