/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.datagen.trait_pools;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TraitPoolEntry {

	private final int weight;
	private List<PlanetTrait> values = new LinkedList<>();

	public TraitPoolEntry(int weight, PlanetTrait... values) {
		this.weight = weight;
		for (PlanetTrait value : values) {
			this.values.add(value);
		}
	}

	public JsonElement serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("weight", weight);
		JsonArray array = new JsonArray();
		values.forEach(trait -> array.add(trait.getRegistryName().toString()));
		obj.add("values", array);
		return obj;
	}

}
