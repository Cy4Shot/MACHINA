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

package com.machina.api.world.data;

import static com.machina.api.ModIDs.MACHINA;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.WillNotClose;

import com.machina.api.util.MachinaRL;
import com.machina.api.util.StringUtils;
import com.machina.config.CommonConfig;
import com.matyrobbrt.lib.nbt.BaseNBTMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class StarchartData extends WorldSavedData {

	// Variables + Constructor
	private static final String ID = MACHINA + "_starchart";

	private final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> starchart = new BaseNBTMap<>(
			rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
			nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::fromNBT);

	private boolean isGenerated;

	public StarchartData(String n) {
		super(n);
		isGenerated = false;
	}

	// Instance
	public static StarchartData getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new StarchartData(ID), ID);
	}

	// Save + Load
	@Override
	public void load(CompoundNBT nbt) {
		isGenerated = nbt.getBoolean("generated");
		starchart.deserializeNBT(nbt.getCompound("starchart"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("generated", isGenerated);
		nbt.put("starchart", starchart.serializeNBT());
		return nbt;
	}

	// This seems very bad... anything could make the map null
	/*
	 * public void setStarchart(Map<ResourceLocation, PlanetData> sc) { starchart =
	 * sc; this.setDirty(); }
	 */

	public Map<ResourceLocation, PlanetData> getStarchart() { return starchart; }

	public void setGenerated(boolean gen) {
		isGenerated = gen;
		this.setDirty();
	}

	public boolean getGenerated() { return isGenerated; }

	public void generateIf(long seed) {
		if (!isGenerated) {

			Random rand = new Random(seed);
			int min = CommonConfig.minPlanets.get();
			int max = CommonConfig.maxPlanets.get();
			int num = min + rand.nextInt(max - min);

			for (int i = 0; i < num; i++) {
				starchart.put(new MachinaRL(i), PlanetData.fromRand(rand));
			}

			isGenerated = true;
			this.setDirty();
		}
	}

	// Static Getters
	public static Map<ResourceLocation, PlanetData> getStarchartForServer(MinecraftServer server) {
		return StarchartData.getDefaultInstance(server).getStarchart();
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < starchart.size(); i++) {
			PlanetData p = starchart.values().stream().collect(Collectors.toList()).get(i);
			StringUtils.printlnUtf8((i == starchart.values().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + p.getName());
			for (int j = 0; j < p.getTraits().size(); j++) {
				StringUtils.printlnUtf8((i == starchart.values().size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.getTraits().get(j).toString());
			}
		}
	}

}
