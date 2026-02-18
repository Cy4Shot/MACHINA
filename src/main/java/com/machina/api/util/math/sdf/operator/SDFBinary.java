package com.machina.api.util.math.sdf.operator;

import com.machina.api.util.math.sdf.SDF;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SDFBinary extends SDF {
	protected final SDF sourceA;
	protected final SDF sourceB;
	protected boolean firstValue;

	public SDFBinary(SDF sourceA, SDF sourceB) {
		this.sourceA = sourceA;
		this.sourceB = sourceB;
	}

	protected void selectValue(float a, float b) {
		firstValue = a < b;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		if (firstValue) {
			return sourceA.getBlockState(pos);
		} else {
			return sourceB.getBlockState(pos);
		}
	}
}
