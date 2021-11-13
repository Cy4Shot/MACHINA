package com.cy4.machina.api.datagen.trait_pools;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TraitPool {

	private List<TraitPoolEntry> entries = new LinkedList<>();

	private final int minRolls;
	private final int maxRolls;

	public TraitPool(int minRolls, int maxRolls) {
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
	}

	public TraitPool withEntries(TraitPoolEntry... entries) {
		for (TraitPoolEntry entry : entries) {
			this.entries.add(entry);
		}
		return this;
	}

	public JsonElement serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("minRolls", minRolls);
		obj.addProperty("maxRolls", maxRolls);
		JsonArray array = new JsonArray();
		entries.forEach(entry -> array.add(entry.serialize()));
		obj.add("entries", array);
		return obj;
	}

}
