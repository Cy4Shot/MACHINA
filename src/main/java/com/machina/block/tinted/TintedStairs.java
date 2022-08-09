package com.machina.block.tinted;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class TintedStairs extends StairsBlock implements ITinted{

	public TintedStairs(BlockState pBaseState, Properties pProperties) {
		super(pBaseState, pProperties);
	}

	public TintedStairs(Supplier<BlockState> state, Properties properties) {
		super(state, properties);
	}
}
