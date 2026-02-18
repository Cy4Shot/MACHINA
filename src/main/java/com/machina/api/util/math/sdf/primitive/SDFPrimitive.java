package com.machina.api.util.math.sdf.primitive;

import java.util.function.Function;

import com.machina.api.util.math.sdf.SDF;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SDFPrimitive extends SDF {
	protected Function<BlockPos, BlockState> placerFunction;

	public SDFPrimitive setBlock(Function<BlockPos, BlockState> placerFunction) {
		this.placerFunction = placerFunction;
		return this;
	}

	public SDFPrimitive setBlock(BlockState state) {
		this.placerFunction = p -> state;
		return this;
	}

	public SDFPrimitive setBlock(Block block) {
		this.placerFunction = p -> block.defaultBlockState();
		return this;
	}

	public BlockState getBlockState(BlockPos pos) {
		return placerFunction.apply(pos);
	}
}
