/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
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
