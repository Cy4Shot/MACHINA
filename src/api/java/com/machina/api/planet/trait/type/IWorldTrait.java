package com.machina.api.planet.trait.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import com.machina.api.world.DynamicDimensionChunkGenerator;
import com.machina.api.world.PlanetGenStage;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public interface IWorldTrait extends IPlanetTraitType {
	/**
	 * This method will be the first thing run during world generation. It will
	 * update settings in the chunk generator.
	 */
	default void updateNoiseSettings(DynamicDimensionChunkGenerator chunkGenerator) {
	}

	/**
	 * This method will generate blocks given the generation stage.
	 * 
	 * @return Boolean: Success?
	 */
	default boolean modify(PlanetGenStage stage, DynamicDimensionChunkGenerator chunkGenerator,
			WorldGenRegion worldGenRegion, IChunk chunk, SharedSeedRandom rand, long seed) {
		return false;
	}
	
	default Collection<Supplier<ConfiguredCarver<?>>> addCarvers(DynamicDimensionChunkGenerator chunkGenerator, SharedSeedRandom rand, long seed) {
		return new ArrayList<>();
	}
}
