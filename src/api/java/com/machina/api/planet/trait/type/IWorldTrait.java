package com.machina.api.planet.trait.type;

import com.machina.api.world.DynamicDimensionChunkGenerator;
import com.machina.api.world.PlanetGenStage;

import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;

public interface IWorldTrait {
	/**
	 * This method will be the first thing run during world generation. It will
	 * update settings in the chunk generator.
	 */
	default public void updateNoiseSettings(DynamicDimensionChunkGenerator chunkGenerator) {
	}

	/**
	 * This method will generate blocks given the generation stage.
	 * 
	 * @return Boolean: Success?
	 */
	default public boolean modify(PlanetGenStage stage, DynamicDimensionChunkGenerator chunkGenerator,
			WorldGenRegion worldGenRegion, IChunk chunk, long seed) {
		return false;
	}
}
