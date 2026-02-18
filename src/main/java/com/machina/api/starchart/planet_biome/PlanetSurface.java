package com.machina.api.starchart.planet_biome;

import java.util.List;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public interface PlanetSurface {

	@FunctionalInterface
	public interface PlanetSurfaceGetter {
		BlockState getState(int x, int y, int z, NormalNoise noise);
	}

	PlanetSurfaceGetter create(List<BlockState> blocks);
}