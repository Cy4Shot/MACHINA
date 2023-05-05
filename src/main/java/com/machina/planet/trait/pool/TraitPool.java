package com.machina.planet.trait.pool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.machina.planet.trait.Trait;
import com.machina.registration.registry.TraitRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;

public class TraitPool {

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

	public List<Trait> allTraits() {
		final List<Trait> list = new LinkedList<>();
		entries.stream().map(entry -> entry.values).forEach(stringList -> {
			stringList.forEach(stringTrait -> {
				Trait trait = TraitRegistry.REGISTRY.getValue(new ResourceLocation(stringTrait));
				if (trait.exists())
					list.add(trait);
			});
		});
		return list;
	}
}
