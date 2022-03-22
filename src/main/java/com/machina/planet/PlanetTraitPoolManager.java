package com.machina.planet;

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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Machina.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlanetTraitPoolManager extends JsonReloadListener {

	public static PlanetTraitPoolManager INSTANCE = new PlanetTraitPoolManager();
	
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

			if (resourcelocation.getPath().startsWith("_")) {
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.
			}

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

			traits = ImmutableMap.copyOf(map);
		}
	}

	public PlanetTraitPool getPool(ResourceLocation loc) {
		return traits.get(loc);
	}

	public void forEach(BiConsumer<? super ResourceLocation, ? super PlanetTraitPool> c) {
		traits.forEach(c);
	}

	public Collection<PlanetTraitPool> getPools() { return traits.values(); }

	public Set<Entry<ResourceLocation, PlanetTraitPool>> getEntrySet() { return traits.entrySet(); }

}
