package com.machina.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SmallFlowerBlock extends BushBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public SmallFlowerBlock(Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public @NotNull BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public @NotNull BlockState mirror(BlockState state, Mirror mir) {
		return state.rotate(mir.getRotation(state.getValue(FACING)));
	}

	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext collision) {
		return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	}

	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_272634_) {
		p_272634_.add(FACING);
	}
}
