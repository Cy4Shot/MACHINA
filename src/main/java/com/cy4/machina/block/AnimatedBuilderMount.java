package com.cy4.machina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class AnimatedBuilderMount extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public AnimatedBuilderMount(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVATED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(ACTIVATED);
    }
}
