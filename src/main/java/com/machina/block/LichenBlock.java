package com.machina.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class LichenBlock extends MultifaceBlock {

	public static final MapCodec<LichenBlock> CODEC = simpleCodec(LichenBlock::new);
	private final MultifaceSpreader spreader = new MultifaceSpreader(this);

	public LichenBlock(Properties properties) {
		super(properties);
	}

	@Override
	public MapCodec<LichenBlock> codec() {
		return CODEC;
	}

	@Override
	public MultifaceSpreader getSpreader() {
		return this.spreader;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		boolean hasAnyFace = false;
		for (Direction direction : DIRECTIONS) {
			if (hasFace(state, direction)) {
				BlockPos attachedPos = pos.relative(direction);
				if (!canAttachTo(level, direction, attachedPos, level.getBlockState(attachedPos))) {
					return false;
				}
				hasAnyFace = true;
			}
		}
		return hasAnyFace;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return !context.getItemInHand().is(this.asItem()) || super.canBeReplaced(state, context);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
			BlockPos pos, BlockPos neighborPos) {
		if (!hasFace(state, direction)) {
			return state;
		}
		return canAttachTo(level, direction, neighborPos, neighborState) ? state : removeFace(state, direction);
	}

	private BlockState removeFace(BlockState state, Direction direction) {
		BlockState newState = state.setValue(getFaceProperty(direction), false);
		return hasAnyFace(newState) ? newState : Blocks.AIR.defaultBlockState();
	}

	public static boolean canAttachTo(BlockGetter level, Direction direction, BlockPos attachedPos,
			BlockState attachedState) {
		return Block.isFaceFull(attachedState.getBlockSupportShape(level, attachedPos), direction.getOpposite())
				|| Block.isFaceFull(attachedState.getCollisionShape(level, attachedPos), direction.getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
	}
}
