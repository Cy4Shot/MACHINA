package com.machina.block;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MachinaHangingWallSignBlock extends WallHangingSignBlock {
	public MachinaHangingWallSignBlock(Properties props, WoodType type) {
		super(type, props);
	}

	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return Objects.requireNonNull(BlockEntityInit.HANGING_SIGN.get().create(pos, state));
	}
}
