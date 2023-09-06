package com.machina.api.multiblock;

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
import com.machina.api.multiblock.Multiblock.MultiblockJsonInfo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class MultiblockLoader extends SimpleJsonResourceReloadListener {
	public static MultiblockLoader INSTANCE = new MultiblockLoader();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private Map<ResourceLocation, Multiblock> multiblocks = ImmutableMap.of();

	public MultiblockLoader() {
		super(GSON, "multiblock");
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

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> entries, ResourceManager man, ProfilerFiller profiler) {
		Map<ResourceLocation, Multiblock> map = Maps.newHashMap();
		for (Entry<ResourceLocation, JsonElement> entry : entries.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			Machina.LOGGER.info("Found Multiblock: " + entry.getKey().toString());
			if (resourcelocation.getPath().startsWith("_")) {
				continue;
			}
			try {
				MultiblockJsonInfo data = GSON.fromJson(entry.getValue(), MultiblockJsonInfo.class);
				if (data == null) {
					continue;
				}
				map.put(resourcelocation, data.cast());
			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Machina.LOGGER.error("Parsing error loading multiblock {}", resourcelocation, jsonparseexception);
			}
			multiblocks = ImmutableMap.copyOf(map);
		}
	}
}