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

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TraitPool {

	private List<TraitPoolEntry> entries = new LinkedList<>();

	private final int minRolls;
	private final int maxRolls;

	public TraitPool(int minRolls, int maxRolls) {
		this.minRolls = minRolls;
		this.maxRolls = maxRolls;
	}

	public TraitPool withEntries(TraitPoolEntry... entries) {
		for (TraitPoolEntry entry : entries) {
			this.entries.add(entry);
		}
		return this;
	}

	public JsonElement serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("minRolls", minRolls);
		obj.addProperty("maxRolls", maxRolls);
		JsonArray array = new JsonArray();
		entries.forEach(entry -> array.add(entry.serialize()));
		obj.add("entries", array);
		return obj;
	}

}
