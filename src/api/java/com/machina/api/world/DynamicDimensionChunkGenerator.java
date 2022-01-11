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

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.machina.api.ModIDs;
import com.machina.api.planet.attribute.PlanetAttributeList;
import com.machina.api.planet.trait.type.IWorldTrait;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.PlanetUtils;
import com.machina.api.world.data.StarchartData;
import com.machina.init.PlanetAttributeTypesInit;
import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.registries.ForgeRegistries;

// https://gist.github.com/Commoble/7db2ef25f94952a4d2e2b7e3d4be53e0
public class DynamicDimensionChunkGenerator extends ChunkGenerator {

	public static final Codec<DynamicDimensionChunkGenerator> CODEC = RegistryLookupCodec
			.create(Registry.BIOME_REGISTRY)
			.xmap(DynamicDimensionChunkGenerator::new, DynamicDimensionChunkGenerator::getBiomeRegistry).codec();

	private final Registry<Biome> biomes;
	private final INoiseGenerator surfaceNoise;
	private final int baseHeight = 60;

	public int seaLevel = -1;
	public float heightMultiplier = 1f;

	public List<? extends IWorldTrait> traits;
	public PlanetAttributeList attributes;
	public int id;
	public long seed = 0L;

	public Registry<Biome> getBiomeRegistry() {
		return biomes;
	}

	public DynamicDimensionChunkGenerator(MinecraftServer server, RegistryKey<Dimension> key) {
		this(server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
		id = PlanetUtils.getIdDim(key);
		this.traits = StarchartData.getDefaultInstance(server).getTraitsForType(key.location(), IWorldTrait.class);
		this.attributes = StarchartData.getStarchartForServer(server).get(key.location()).getAttributes();
		traits.forEach(t -> t.updateNoiseSettings(this));

	}

	public DynamicDimensionChunkGenerator(Registry<Biome> biomes) {
		super(new SingleBiomeProvider(
				biomes.getOrThrow(RegistryKey.create(Registry.BIOME_REGISTRY, new MachinaRL(ModIDs.MACHINA)))),
				new DynamicStructureSettings());
		this.biomes = biomes;
		surfaceNoise = (new PerlinNoiseGenerator(new SharedSeedRandom(), IntStream.rangeClosed(-3, 0)));
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
		final BlockState baseBlock = ForgeRegistries.BLOCKS
				.getValue(new ResourceLocation(attributes.getValue(PlanetAttributeTypesInit.BASE_BLOCK)))
				.defaultBlockState();
		final SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);

		/*
		 * 1. GENERATE BASE LAYER
		 */

		ChunkPos chunkpos = chunk.getPos();
		int i = chunkpos.x;
		int j = chunkpos.z;
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
					chunk.setBlockState(blockpos$mutable.set(i1, y, j1), baseBlock, false);
				}
				if (yPos < seaLevel) {
					for (int y = yPos; y < seaLevel; y++) {
						chunk.setBlockState(blockpos$mutable.set(i1, y, j1), Blocks.WATER.defaultBlockState(), false);
					}
				}
			}
		}

		this.placeBedrock(chunk, sharedseedrandom);

		traits.forEach(t -> t.modify(PlanetGenStage.BASE, this, worldGenRegion, chunk, sharedseedrandom, seed));

		/*
		 * 2. GENERATE CARVER LAYER
		 */

		traits.forEach(t -> t.modify(PlanetGenStage.CARVER, this, worldGenRegion, chunk, sharedseedrandom, seed));

		/*
		 * 3. GENERATE STRUCTURE LAYER
		 */

		traits.forEach(t -> t.modify(PlanetGenStage.STRUCTURE, this, worldGenRegion, chunk, sharedseedrandom, seed));

		/*
		 * 4. GENERATE FEATURE LAYER
		 */

		traits.forEach(t -> t.modify(PlanetGenStage.FEATURE, this, worldGenRegion, chunk, sharedseedrandom, seed));

		/*
		 * 5. GENERATE DECORATION LAYER
		 */

		traits.forEach(t -> t.modify(PlanetGenStage.DECORATION, this, worldGenRegion, chunk, sharedseedrandom, seed));
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

	@Override
	public IBlockReader getBaseColumn(int x, int z) {
		return new Blockreader(new BlockState[0]);
	}
	
	public int getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(int seaLevel) {
		this.seaLevel = seaLevel;
	}

	public float getHeightMultiplier() {
		return heightMultiplier;
	}

	public void setHeightMultiplier(float heightMultiplier) {
		this.heightMultiplier = heightMultiplier;
	}

	public List<? extends IWorldTrait> getTraits() {
		return traits;
	}

	public void setTraits(List<? extends IWorldTrait> traits) {
		this.traits = traits;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public Registry<Biome> getBiomes() {
		return biomes;
	}

	public INoiseGenerator getSurfaceNoise() {
		return surfaceNoise;
	}

	public int getBaseHeight() {
		return baseHeight;
	}

	public PlanetAttributeList getAttributes() {
		return attributes;
	}
}
