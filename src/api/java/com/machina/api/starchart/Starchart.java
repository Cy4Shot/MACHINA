/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

package com.machina.api.starchart;

import java.util.Optional;
import java.util.Random;

import com.machina.api.nbt.BaseNBTMap;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.StringUtils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.util.INBTSerializable;

public class Starchart implements INBTSerializable<CompoundNBT> {

	public static int maxPlanets;
	public static int minPlanets;

	public final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> planets = new BaseNBTMap<>(
			rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
			nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::deserialize);

	public Starchart() {
	}

	public Starchart(CompoundNBT nbt) {
		this();
		this.deserializeNBT(nbt);
	}

	public void generateStarchart(long seed) {
		planets.clear();
		Random rand = new Random(seed);
		int numPlanets = rand.nextInt(maxPlanets - minPlanets + 1) + minPlanets;

		for (int i = 0; i < numPlanets; i++) {
			planets.put(new MachinaRL(i), new PlanetData(rand));
		}
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < planets.size(); i++) {
			PlanetData p = planets.getValues().get(i);
			StringUtils.printlnUtf8((i == planets.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + p.getName());
			for (int j = 0; j < p.getTraits().size(); j++) {
				StringUtils.printlnUtf8((i == planets.size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.getTraits().get(j).toString());
			}
		}
	}

	// Serialize all planet data
	@Override
	public CompoundNBT serializeNBT() {
		return serializeNBT(new CompoundNBT());
	}

	public CompoundNBT serializeNBT(CompoundNBT nbt) {
		nbt.put("planets", planets.serializeNBT());
		return nbt;
	}

	// Create new planet data per tag
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		planets.deserializeNBT(nbt.getCompound("planets"));
	}

	/**
	 * Gets the current data of the dimension or creates it if not present. <br>
	 * {@link Optional} because it can still <i>somehow</i> be null after being
	 * created
	 * 
	 * @param dimID
	 * @return
	 */
	public Optional<PlanetData> getDimensionData(ResourceLocation dimID) {
		return Optional.ofNullable(planets.get(dimID));
	}

	public Optional<PlanetData> getDimensionDataOrCreate(ResourceLocation dimID) {
		planets.computeIfAbsent(dimID, key -> new PlanetData());
		return Optional.ofNullable(planets.get(dimID));
	}
}
