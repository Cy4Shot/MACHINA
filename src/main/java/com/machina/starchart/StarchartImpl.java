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

package com.machina.starchart;

import static com.machina.api.ModIDs.MACHINA;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.WillNotClose;

import com.machina.api.api_extension.ApiExtension;
import com.machina.api.api_extension.ApiExtensions;
import com.machina.api.planet.trait.type.IPlanetTraitType;
import com.machina.api.starchart.Starchart;
import com.machina.api.util.MachinaRL;
import com.machina.api.util.StringUtils;
import com.machina.api.world.data.PlanetData;
import com.machina.config.CommonConfig;
import com.machina.init.PlanetAttributeTypesInit;
import com.matyrobbrt.lib.nbt.BaseNBTMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class StarchartImpl extends WorldSavedData implements Starchart {

	public static void registerAPIExtension() {
		ApiExtensions.registerExtension(Starchart.StarchartGetter.class, server -> server.getLevel(World.OVERWORLD)
				.getDataStorage().computeIfAbsent(() -> new StarchartImpl(ID), ID));
	}

	private static final String ID = MACHINA + "_starchart";

	/**
	 * Use an API Extension in order for the API to have access to the starchart
	 */
	@ApiExtension(StarchartGetter.class)
	private static final StarchartGetter API_STARCHART_GETTER = server -> server.getLevel(World.OVERWORLD)
			.getDataStorage().computeIfAbsent(() -> new StarchartImpl(ID), ID);

	private final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> planetData = new BaseNBTMap<>(
			rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
			nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::fromNBT);

	private boolean isGenerated;

	private StarchartImpl(String name) {
		super(name);
		isGenerated = false;
	}

	// Instance
	public static StarchartImpl getDefaultInstance(@WillNotClose MinecraftServer server) {
		ServerWorld w = server.getLevel(World.OVERWORLD);
		return w.getDataStorage().computeIfAbsent(() -> new StarchartImpl(ID), ID);
	}

	// Save + Load
	@Override
	public void load(CompoundNBT nbt) {
		isGenerated = nbt.getBoolean("generated");
		planetData.deserializeNBT(nbt.getCompound("PlanetData"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.putBoolean("generated", isGenerated);
		nbt.put("PlanetData", planetData.serializeNBT());
		return nbt;
	}

	@Override
	public <TYPE extends IPlanetTraitType> List<TYPE> getTraitsForType(ResourceLocation dimensionId,
			Class<TYPE> typeClass) {
		return planetData.computeIfAbsent(dimensionId, k -> new PlanetData()).getTraits().stream()
				.filter(typeClass::isInstance).map(typeClass::cast).collect(Collectors.toList());
	}

	@Override
	public PlanetData getDataForLevel(ResourceLocation dimensionId) {
		return planetData.get(dimensionId);
	}

	public void setGenerated(boolean gen) {
		isGenerated = gen;
		this.setDirty();
	}

	@Override
	public void setDirty() {
		super.setDirty();
		cache();
	}

	public void cache() {
		// TODO Here we should cache the trait types, because streams are not the
		// fastest things in the world
	}

	public boolean getGenerated() {
		return isGenerated;
	}

	public void generateIf(long seed) {
		if (!isGenerated) {

			Random rand = new Random(seed);
			int min = CommonConfig.minPlanets.get();
			int max = CommonConfig.maxPlanets.get();
			int num = min + rand.nextInt(max - min);

			for (int i = 0; i < num; i++) {
				planetData.put(new MachinaRL(i), PlanetData.fromRand(rand));
			}

			isGenerated = true;
			this.setDirty();
		}
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < planetData.size(); i++) {
			PlanetData p = planetData.values().stream().collect(Collectors.toList()).get(i);
			StringUtils.printlnUtf8((i == planetData.values().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + p.getAttributeFormatted(PlanetAttributeTypesInit.PLANET_NAME));
			for (int j = 0; j < p.getTraits().size(); j++) {
				StringUtils.printlnUtf8((i == planetData.values().size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.getTraits().get(j).toString());
			}
		}
	}

}
