package com.machina.api.starchart.planet_biome.placement;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public interface PlacementModifier {

	public static boolean applyAll(List<PlacementModifier> modifiers, BlockPos.MutableBlockPos pos, RandomSource rand, WorldGenLevel level) {
		for (PlacementModifier modifier : modifiers) {
			if (!modifier.apply(pos, rand, level)) {
				return false;
			}
		}
		return true;
	}

	boolean apply(BlockPos.MutableBlockPos pos, RandomSource rand, WorldGenLevel level);
}