package com.machina.api.util.math.sdf.post;

import java.util.function.Predicate;

import com.machina.api.util.math.sdf.PosInfo;
import com.machina.api.util.math.sdf.SDF.SDFPostProcessor;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public record SDFFruitPlacer(RandomSource random, Direction dir, float chance, BlockState fruit,
		Predicate<BlockState> attachable) implements SDFPostProcessor {

	public boolean isAir(PosInfo pos, int d) {
		return pos.getState(pos.getPos().relative(dir, d)).isAir();
	}

	@Override
	public Pair<BlockPos, BlockState> extra(PosInfo posInfo) {
		BlockPos p = posInfo.getPos();
		if (attachable.test(posInfo.getState(p)) && random.nextFloat() < chance) {
			if (isAir(posInfo, 1) && isAir(posInfo, 2)) {
				return Pair.of(p.relative(dir), fruit);
			}
		}
		return null;
	}
}
