/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.machina.api.ModIDs;
import com.machina.api.planet.trait.DefaultPlanetTraits;
import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.PlanetUtils;
import com.machina.api.world.data.StarchartData;
import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.Blockreader;
import net.minecraft.world.Dimension;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class DynamicDimensionChunkGenerator extends ChunkGenerator {

	public static final Codec<DynamicDimensionChunkGenerator> CODEC = RegistryLookupCodec
			.create(Registry.BIOME_REGISTRY)
			.xmap(DynamicDimensionChunkGenerator::new, DynamicDimensionChunkGenerator::getBiomeRegistry).codec();

	private final Registry<Biome> biomes;
	private final INoiseGenerator surfaceNoise;
	private final int baseHeight = 60;

	private int seaLevel = 40;
	private float heightMultiplier = 1f;
	private boolean freezing = false;

	public int id;
	private List<PlanetTrait> traits = new ArrayList<>();
	public long seed = 0L;

	public Registry<Biome> getBiomeRegistry() { return biomes; }

	public DynamicDimensionChunkGenerator(MinecraftServer server, RegistryKey<Dimension> key) {
		this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
		id = PlanetUtils.getIdDim(key);
		traits.clear();
		/*
		 * What on earth have you done cy4. I keep getting IOB exceptions
		 */
		StarchartData.getStarchartForServer(server).getDimensionData(key.location()).ifPresent(data -> traits.addAll(data.getTraits()));
		updateTraitValues();
	}

	public DynamicDimensionChunkGenerator(Registry<Biome> biomes) {
		super(new SingleBiomeProvider(
				biomes.getOrThrow(RegistryKey.create(Registry.BIOME_REGISTRY, new MachinaRL(ModIDs.MACHINA)))),
				new DynamicStructureSettings());
		this.biomes = biomes;
		surfaceNoise = (new PerlinNoiseGenerator(new SharedSeedRandom(), IntStream.rangeClosed(-3, 0)));
	}

	// TODO dont hard code this, its bad :(
	public void updateTraitValues() {
		if (traits.contains(DefaultPlanetTraits.FLAT)) {
			heightMultiplier = 0.5f;
		} else if (traits.contains(DefaultPlanetTraits.HILLY)) {
			heightMultiplier = 2.5f;
		} else if (traits.contains(DefaultPlanetTraits.MOUNTAINOUS)) {
			heightMultiplier = 7.5f;
		}

		if (traits.contains(DefaultPlanetTraits.LANDMMASS)) {
			seaLevel = -1;
		} else if (traits.contains(DefaultPlanetTraits.WATER_WORLD)) {
			seaLevel = 85;
		} else if (traits.contains(DefaultPlanetTraits.CONTINENTALL)) {
			seaLevel = baseHeight;
		}

		if (traits.contains(DefaultPlanetTraits.FROZEN)) {
			freezing = true;
		}
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		this.seed = seed;
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, IChunk chunk) {
		long seed = worldGenRegion.getLevel().getSeed();

		ChunkPos chunkpos = chunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
		sharedseedrandom.setBaseChunkSeed(i, j);
		ChunkPos chunkpos1 = chunk.getPos();
		int k = chunkpos1.getMinBlockX();
		int l = chunkpos1.getMinBlockZ();
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		for (int i1 = 0; i1 < 16; ++i1) {
			for (int j1 = 0; j1 < 16; ++j1) {
				int k1 = k + i1;
				int l1 = l + j1;
				double d1 = surfaceNoise.getSurfaceNoiseValue(k1 * 0.0625D, l1 * 0.0625D, 0.0625D, i1 * 0.0625D)
						* 15.0D;

				int yPos = baseHeight + (int) (d1 * heightMultiplier);
				for (int y = 0; y < yPos; y++) {
					// System.out.println(traits.contains(DefaultPlanetTraits.MOUNTAINOUS));
					chunk.setBlockState(blockpos$mutable.set(i1, y, j1), Blocks.STONE.defaultBlockState(), false);
				}
				if (yPos < seaLevel) {
					for (int y = yPos; y < seaLevel; y++) {
						chunk.setBlockState(blockpos$mutable.set(i1, y, j1), Blocks.WATER.defaultBlockState(), false);
					}

					if (freezing) {
						chunk.setBlockState(blockpos$mutable.set(i1, seaLevel, j1),
								(sharedseedrandom.nextInt(2) == 0 ? Blocks.PACKED_ICE : Blocks.ICE).defaultBlockState(),
								false);
					}
				}
			}
		}

		this.placeBedrock(chunk, sharedseedrandom);
	}

	private void placeBedrock(IChunk chunk, Random random) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = chunk.getPos().getMinBlockX();
		int j = chunk.getPos().getMinBlockZ();
		for (BlockPos blockpos : BlockPos.betweenClosed(i, 0, j, i + 15, 0, j + 15)) {
			for (int k1 = 4; k1 >= 0; --k1) {
				if (k1 <= random.nextInt(5)) {
					chunk.setBlockState(blockpos$mutable.set(blockpos.getX(), k1, blockpos.getZ()),
							Blocks.BEDROCK.defaultBlockState(), false);
				}
			}

		}
	}

	@Override
	public void fillFromNoise(IWorld world, StructureManager structures, IChunk chunk) {
		//
	}

	@Override
	public int getBaseHeight(int x, int z, Type heightmapType) {
		return 0;
	}

	// get base column
	@Override
	public IBlockReader getBaseColumn(int x, int z) {
		return new Blockreader(new BlockState[0]);
	}
}
