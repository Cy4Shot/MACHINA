package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SDFUnary extends SDF {
	protected final SDF source;

	public SDFUnary(SDF source) {
		this.source = source;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return source.getBlockState(pos);
	}
}
