package com.machina.datagen.trait_pools;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.machina.planet.trait.Trait;

public class TraitPoolEntry {

	private final int weight;
	private List<Trait> values = new LinkedList<>();

	public TraitPoolEntry(int weight, Trait... values) {
		this.weight = weight;
		for (Trait value : values) {
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
