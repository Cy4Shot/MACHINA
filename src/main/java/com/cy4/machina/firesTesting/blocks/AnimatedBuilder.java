package com.cy4.machina.firesTesting.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class AnimatedBuilder extends Block
{
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public AnimatedBuilder(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVATED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(ACTIVATED);
    }
}
