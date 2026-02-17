package com.machina.block;

import com.machina.api.util.block.BlockProperties;
import com.machina.api.util.math.DirUtil;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PebbleBlock extends HorizontalDirectionalBlock {

    public static final IntegerProperty VARIANT = BlockProperties.VARIANT_4;

    public PebbleBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(VARIANT, 0));
    }

    public static void placeAt(LevelAccessor level, BlockState state, BlockPos pos, int flags) {
        RandomSource r = level.getRandom();
        state.setValue(FACING, DirUtil.getRandomHorizontal(r)).setValue(VARIANT, r.nextInt(4));
        level.setBlock(pos, state, flags);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        RandomSource r = ctx.getLevel().getRandom();
        return this.defaultBlockState().setValue(FACING, DirUtil.getRandomHorizontal(r)).setValue(VARIANT,
                r.nextInt(4));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos,
                                        @NotNull CollisionContext collision) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, VARIANT);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction dir, @NotNull BlockState state2,
                                           @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos pos2) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, dir, state2, level, pos, pos2);
    }
    
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(PebbleBlock::new);
    }
}
