/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.datagen.trait_pools;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

public abstract class TraitPoolsGenProvider implements IDataProvider {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	public final DataGenerator generator;
	public final String modid;

	private HashMap<String, TraitPool> pools = new HashMap<>();

	protected TraitPoolsGenProvider(DataGenerator generator, String modid) {
		this.generator = generator;
		this.modid = modid;
	}

	@Override
	public void run(DirectoryCache pCache) throws IOException {
		addPools();
		write(pCache);
	}

	private void write(DirectoryCache cache) {
		Path outputFolder = generator.getOutputFolder();
		pools.forEach((name, pool) -> {
			Path path = outputFolder.resolve("data/" + modid + "/trait_pools/" + name + ".json");
			try {
				IDataProvider.save(GSON, cache, pool.serialize(), path);
			} catch (IOException e) {
				LOGGER.error("Couldn't generate planet trait pool!", path, e);
			}
		});
	}

	@Override
	public String getName() { return "TraitPools"; }

	protected abstract void addPools();

	protected void addPool(String name, TraitPool pool) {
		pools.put(name, pool);
	}

}
