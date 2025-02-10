package com.machina.api.util.loader;

import java.util.Collection;
import java.util.HashMap;
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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public class JsonLoader<T> extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private Map<ResourceLocation, T> registry = ImmutableMap.of();
	private final String name;
	private final Class<? extends JsonInfo<T>> clazz;

	public JsonLoader(String name, Class<? extends JsonInfo<T>> clazz) {
		super(GSON, name);
		this.name = name;
		this.clazz = clazz;
	}

	public void reload(ResourceManager man) {
		Map<ResourceLocation, JsonElement> map = new HashMap<>();
		scanDirectory(man, name, GSON, map);
		apply(map, man, InactiveProfiler.INSTANCE);
	}

	public T get(ResourceLocation loc) {
		return registry.get(loc);
	}

	public void forEach(BiConsumer<? super ResourceLocation, ? super T> c) {
		registry.forEach(c);
	}

	public Set<ResourceLocation> getAllLoc() {
		return registry.keySet();
	}

	public Collection<T> getAll() {
		return registry.values();
	}

	public Set<Entry<ResourceLocation, T>> getEntrySet() {
		return registry.entrySet();
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> entries, @NotNull ResourceManager man,
			@NotNull ProfilerFiller profiler) {
		Map<ResourceLocation, T> map = Maps.newHashMap();
		for (Entry<ResourceLocation, JsonElement> entry : entries.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			Machina.LOGGER.info("Found {} : {}", name, entry.getKey().toString());
			if (resourcelocation.getPath().startsWith("_")) {
				continue;
			}
			try {
				JsonInfo<T> data = GSON.fromJson(entry.getValue(), clazz);
				if (data == null) {
					continue;
				}
				map.put(resourcelocation, data.cast());
			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				Machina.LOGGER.error("Parsing error loading {} {}", name, resourcelocation, jsonparseexception);
			}
			registry = ImmutableMap.copyOf(map);
		}
	}
}