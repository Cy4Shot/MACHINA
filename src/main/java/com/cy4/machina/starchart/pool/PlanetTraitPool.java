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

package com.cy4.machina.starchart.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;

public class PlanetTraitPool {

	public int minRolls;
	public int maxRolls;
	public List<PlanetTraitPoolEntry> entries;

	public static class PlanetTraitPoolEntry {

		public int weight;
		public List<String> values;
	}

	public WeightedList<List<String>> createWeightedList() {
		WeightedList<List<String>> list = new WeightedList<>();
		entries.forEach(entry -> {
			list.add(entry.values, entry.weight);
		});
		return list;
	}

	// This seems more dodgy than it should be..
	public List<ResourceLocation> roll(Random r) {
		int rolls = (r.nextInt(maxRolls - minRolls + 1) + minRolls); // Inclusive
		List<ResourceLocation> output = new ArrayList<>();
		WeightedList<List<String>> values = createWeightedList();

		for (int i = 0; i < rolls; i++) {
			List<String> value = values.getOne(r);
			output.add(new ResourceLocation(value.get(r.nextInt(value.size()))));
			values.entries.removeIf(entry -> entry.getData().equals(value));
		}

		return output;
	}
}
