package com.machina.api.block;

import com.machina.api.util.block.BlockProperties;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class LitMachineBlock extends MachineBlock {

	public static final BooleanProperty LIT = BlockProperties.LIT;;

	protected LitMachineBlock(Properties props) {
		super(props);

		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
	}

	@Override
	protected boolean isTickable() {
		return true;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(LIT);
		super.createBlockStateDefinition(builder);
	}
}
