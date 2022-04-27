package com.machina.planet.trait.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import com.machina.world.DynamicDimensionChunkGenerator;

import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public interface IWorldTrait extends IPlanetTraitType {
	/**
	 * This method will be the first thing run during world generation. It will
	 * update settings in the chunk generator.
	 */
	default void init(DynamicDimensionChunkGenerator chunkGenerator) {
	}
	
	default Collection<Supplier<ConfiguredCarver<?>>> addCarvers(DynamicDimensionChunkGenerator chunkGenerator) {
		return new ArrayList<>();
	}
	
	default Collection<Supplier<ConfiguredFeature<?, ?>>> addFeatures(DynamicDimensionChunkGenerator chunkGenerator) {
		return new ArrayList<>();
	}
}
