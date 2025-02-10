package com.machina.block;

import com.machina.registration.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MachinaWallSignBlock extends WallSignBlock {
	public MachinaWallSignBlock(Properties props, WoodType type) {
		super(props, type);
	}
	
	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return Objects.requireNonNull(BlockEntityInit.SIGN.get().create(pos, state));
	}
}
