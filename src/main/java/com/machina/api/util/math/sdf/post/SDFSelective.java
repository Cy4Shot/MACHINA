package com.machina.api.util.math.sdf.post;

import java.util.function.Predicate;

import com.machina.api.util.math.sdf.PosInfo;
import com.machina.api.util.math.sdf.SDF.SDFPostProcessor;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record SDFSelective(SDFPostProcessor parent, Predicate<BlockState> test) implements SDFPostProcessor {

	public SDFSelective(SDFPostProcessor parent, Block block) {
		this(parent, state -> state.getBlock().equals(block));
	}

	@Override
	public BlockState apply(PosInfo info) {
		BlockState state = info.getState();
		if (test.test(state)) {
			return parent.apply(info);
		}
		return state;
	}

	@Override
	public Pair<BlockPos, BlockState> extra(PosInfo posInfo) {
		if (test.test(posInfo.getState())) {
			return parent.extra(posInfo);
		}
		return null;
	}
}