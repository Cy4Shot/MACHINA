package com.machina.api.client;

import java.util.HashMap;
import java.util.Map;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeClientSettings;

import net.minecraft.resources.ResourceLocation;

public class ClientBiomeSettings {
	public static Map<ResourceLocation, PlanetBiomeClientSettings> BIOME_SETTINGS = new HashMap<>();
}
