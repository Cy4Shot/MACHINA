package com.cy4.machina.starchart.pool;

import java.util.Map;
import java.util.Map.Entry;

import com.cy4.machina.Machina;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class PlanetTraitPoolManager extends JsonReloadListener {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private Map<ResourceLocation, PlanetTraitPool> traits = ImmutableMap.of();

	public PlanetTraitPoolManager() {
		super(GSON, "trait_pools");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager,
			IProfiler pProfiler) {

		Map<ResourceLocation, PlanetTraitPool> map = Maps.newHashMap();

		for (Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			Machina.LOGGER.info("Found Trait Pool: " + entry.getKey().toString());

			if (resourcelocation.getPath().startsWith("_"))
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.

			try {
				PlanetTraitPool pool = GSON.fromJson(entry.getValue(), PlanetTraitPool.class);
				if (pool == null) {
					Machina.LOGGER.error("Trait Pool is NULL!!!! Bit rude, fix pls.");
					continue;
				}

				map.put(resourcelocation, pool);

			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Machina.LOGGER.error("Parsing error loading trait pool {}", resourcelocation, jsonparseexception);
			}

			this.traits = ImmutableMap.copyOf(map);
		}
	}
	
	public PlanetTraitPool getPool(ResourceLocation loc) {
		return traits.get(loc);
	}

}
