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

package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;
import com.machina.api.world.IDynamicDimensionChunkGenerator;
import com.machina.api.world.PlanetGenStage;

import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.WorldGenRegion;

public class WaterWorldTrait extends PlanetTrait implements IWorldTrait {

	public WaterWorldTrait(int color, String description) {
		super(color, description);
	}
	
	@Override
	public void updateNoiseSettings(IDynamicDimensionChunkGenerator chunkGenerator) {
		chunkGenerator.setSeaLevel(85);
		chunkGenerator.setHeightMultiplier(getColor());
	}
	
	@Override
	public boolean modify(PlanetGenStage stage, IDynamicDimensionChunkGenerator chunkGenerator,
			WorldGenRegion worldGenRegion, IChunk chunk, long seed) {
		// TODO Auto-generated method stub
		return IWorldTrait.super.modify(stage, chunkGenerator, worldGenRegion, chunk, seed);
	}

}
