/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

package com.machina.api.datagen.trait_pools;

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
