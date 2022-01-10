/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Machina Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.machina.api.planet.pool;

import static com.machina.api.ModIDs.MACHINA;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = MACHINA, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlanetTraitPoolManager extends JsonReloadListener {

	public static PlanetTraitPoolManager INSTANCE = new PlanetTraitPoolManager();
	
	private static final Logger LOGGER = LogManager.getLogger();
	
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
			LOGGER.info("Found Trait Pool: " + entry.getKey().toString());

			if (resourcelocation.getPath().startsWith("_")) {
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.
			}

			try {
				PlanetTraitPool pool = GSON.fromJson(entry.getValue(), PlanetTraitPool.class);
				if (pool == null) {
					LOGGER.error("Trait Pool is NULL!!!! Bit rude, fix pls.");
					continue;
				}

				map.put(resourcelocation, pool);

			} catch (IllegalArgumentException | JsonParseException jsonparseexception) {
				LOGGER.error("Parsing error loading trait pool {}", resourcelocation, jsonparseexception);
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
