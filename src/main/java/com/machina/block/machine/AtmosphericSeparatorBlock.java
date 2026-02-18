package com.machina.block.machine;

import com.machina.api.block.MachineBlock;
import com.machina.api.block.entity.MachinaBlockEntity;
import com.machina.block.entity.machine.AtmosphericSeparatorBlockEntity;
import com.machina.registration.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AtmosphericSeparatorBlock extends MachineBlock {

	public AtmosphericSeparatorBlock(Properties props) {
		super(props.noOcclusion().isRedstoneConductor(AtmosphericSeparatorBlock::never)
				.isSuffocating(AtmosphericSeparatorBlock::never).isViewBlocking(AtmosphericSeparatorBlock::never));
	}

	public static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
		return false;
	}

	@Override
	public BlockEntityType<?> getBlockEntityType() {
		return BlockEntityInit.ATMOSPHERIC_SEPARATOR.get();
	}

	@Override
	protected boolean isTickable() {
		return true;
	}

	@Override
	public Class<? extends MachinaBlockEntity> getBlockEntityClass() {
		return AtmosphericSeparatorBlockEntity.class;
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return simpleCodec(AtmosphericSeparatorBlock::new);
	}
}