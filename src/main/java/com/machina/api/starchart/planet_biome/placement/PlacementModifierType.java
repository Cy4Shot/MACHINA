package com.machina.api.starchart.planet_biome.placement;

import java.util.function.Function;

import com.google.gson.JsonObject;

public class PlacementModifierType {
	private final Function<JsonObject, PlacementModifier> factory;

	public PlacementModifierType(Function<JsonObject, PlacementModifier> factory) {
		this.factory = factory;
	}

	public PlacementModifier create(JsonObject json) {
		return factory.apply(json);
	}
}