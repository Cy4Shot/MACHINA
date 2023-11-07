package com.machina.api.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;

public class PlanetHelper {
	public static int getIdDim(ResourceKey<LevelStem> dim) {
		return Integer.valueOf(dim.location().getPath());
	}
}
