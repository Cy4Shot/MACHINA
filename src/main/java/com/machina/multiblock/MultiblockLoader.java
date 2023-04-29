package com.machina.multiblock;

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
import com.machina.multiblock.Multiblock.MultiblockData;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class MultiblockLoader extends JsonReloadListener {
	public static MultiblockLoader INSTANCE = new MultiblockLoader();

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private Map<ResourceLocation, Multiblock> multiblocks = ImmutableMap.of();

	public MultiblockLoader() {
		super(GSON, "multiblocks");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager,
			IProfiler pProfiler) {

		Map<ResourceLocation, Multiblock> map = Maps.newHashMap();

		for (Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			Machina.LOGGER.info("Found Multiblock Pool: " + entry.getKey().toString());

			if (resourcelocation.getPath().startsWith("_")) {
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.
			}

			try {
				MultiblockData data = GSON.fromJson(entry.getValue(), MultiblockData.class);
				if (data == null) {
					continue;
				}
				
				System.out.println(data.cast());

				map.put(resourcelocation, data.cast());

			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Machina.LOGGER.error("Parsing error loading trait pool {}", resourcelocation, jsonparseexception);
			}

			multiblocks = ImmutableMap.copyOf(map);
		}
	}

	public Multiblock getMultiblock(ResourceLocation loc) {
		return multiblocks.get(loc);
	}

	public void forEach(BiConsumer<? super ResourceLocation, ? super Multiblock> c) {
		multiblocks.forEach(c);
	}

	public Collection<Multiblock> getMultiblocks() {
		return multiblocks.values();
	}

	public Set<Entry<ResourceLocation, Multiblock>> getEntrySet() {
		return multiblocks.entrySet();
	}
}
