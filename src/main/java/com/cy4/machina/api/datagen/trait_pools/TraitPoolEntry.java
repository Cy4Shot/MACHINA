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
