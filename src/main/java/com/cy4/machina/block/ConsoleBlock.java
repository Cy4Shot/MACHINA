package com.cy4.machina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class ConsoleBlock extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	public ConsoleBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection());
	}
}
