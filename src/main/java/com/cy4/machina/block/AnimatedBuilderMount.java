/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class AnimatedBuilderMount extends Block {

	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	public AnimatedBuilderMount(Properties properties) {
		super(properties);
		this.registerDefaultState(stateDefinition.any().setValue(ACTIVATED, Boolean.FALSE));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
		stateBuilder.add(ACTIVATED);
	}
}
