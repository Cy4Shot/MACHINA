package com.machina.world.builder;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class DynamicDimensionNoiseSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig> {

	private BlockState[] states;

	public DynamicDimensionNoiseSurfaceBuilder(Codec<SurfaceBuilderConfig> c, BlockState... states) {
		super(c);
		this.states = states;
	}

	public void apply(Random rand, IChunk chunk, Biome biome, int x, int z, int startHeight, double noise,
			BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig c) {
		int i = rand.nextInt(states.length);
		DynamicDimensionSurfaceBuilder.BASE.apply(rand, chunk, biome, x, z, startHeight, noise, defaultBlock,
				defaultFluid, seaLevel, seed,
				new SurfaceBuilderConfig(states[i], c.getUnderMaterial(), c.getUnderwaterMaterial()));
	}
}