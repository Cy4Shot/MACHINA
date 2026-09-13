package com.machina.block;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.machina.block.entity.HightechTurretBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class HightechTurretBlock extends Block implements EntityBlock {

	public HightechTurretBlock(Properties properties) {
		super(properties.noOcclusion());
	}

	@Override
	public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return Objects.requireNonNull(BlockEntityInit.HIGHTECH_TURRET.get().create(pos, state));
	}

	@Override
	protected MapCodec<HightechTurretBlock> codec() {
		return simpleCodec(HightechTurretBlock::new);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
			@NotNull BlockEntityType<T> type) {
		return (level1, pos, state1, be) -> ((HightechTurretBlockEntity) be).tick();
	}
}
