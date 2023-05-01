package com.machina.planet.pool;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.machina.Machina;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class TraitPoolLoader extends JsonReloadListener {

	public static TraitPoolLoader INSTANCE = new TraitPoolLoader();

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private Map<ResourceLocation, TraitPool> traits = ImmutableMap.of();

	public TraitPoolLoader() {
		super(GSON, "trait_pools");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager,
			IProfiler pProfiler) {

		Map<ResourceLocation, TraitPool> map = Maps.newHashMap();

		for (Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			Machina.LOGGER.info("Found Trait Pool: " + entry.getKey().toString());

			if (resourcelocation.getPath().startsWith("_")) {
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.
			}

			try {
				TraitPool pool = GSON.fromJson(entry.getValue(), TraitPool.class);
				if (pool == null) {
					Machina.LOGGER.error("Trait Pool is NULL!!!! Bit rude, fix pls.");
					continue;
				}

				map.put(resourcelocation, pool);

			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Machina.LOGGER.error("Parsing error loading trait pool {}", resourcelocation, jsonparseexception);
			}

			traits = ImmutableMap.copyOf(map);
		}
	}

	public TraitPool getPool(ResourceLocation loc) {
		return traits.get(loc);
	}

	public void forEach(BiConsumer<? super ResourceLocation, ? super TraitPool> c) {
		traits.forEach(c);
	}

	public Collection<TraitPool> getPools() {
		return traits.values();
	}

	public Set<Entry<ResourceLocation, TraitPool>> getEntrySet() {
		return traits.entrySet();
	}

}
