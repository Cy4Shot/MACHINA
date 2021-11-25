/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.starchart;

import java.util.Random;

import com.cy4.machina.api.nbt.BaseNBTMap;
import com.cy4.machina.config.CommonConfig;
import com.cy4.machina.util.MachinaRL;
import com.cy4.machina.util.StringUtils;
import java.util.Optional;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.util.INBTSerializable;

public class Starchart implements INBTSerializable<CompoundNBT> {

	public final BaseNBTMap<ResourceLocation, PlanetData, StringNBT, CompoundNBT> planets = new BaseNBTMap<>(
			rl -> StringNBT.valueOf(rl.toString()), PlanetData::serializeNBT,
			nbt -> new ResourceLocation(nbt.getAsString()), PlanetData::deserialize);

	public Starchart() {
	}

	public Starchart(CompoundNBT nbt) {
		this();
		this.deserializeNBT(nbt);
	}

	public void generateStarchart(long seed) {
		planets.clear();
		Random rand = new Random(seed);
		int numPlanets = rand.nextInt(CommonConfig.MAX_PLANETS.get() - CommonConfig.MIN_PLANETS.get() + 1)
				+ CommonConfig.MIN_PLANETS.get();

		for (int i = 0; i < numPlanets; i++) {
			planets.put(new MachinaRL(i), new PlanetData(rand));
		}
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < planets.size(); i++) {
			PlanetData p = planets.getValues().get(i);
			StringUtils.printlnUtf8((i == planets.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F)
					+ StringUtils.TREE_H + p.getName());
			for (int j = 0; j < p.getTraits().size(); j++) {
				StringUtils.printlnUtf8((i == planets.size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.getTraits().size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.getTraits().get(j).toString());
			}
		}
	}

	// Serialize all planet data
	@Override
	public CompoundNBT serializeNBT() {
		return serializeNBT(new CompoundNBT());
	}

	public CompoundNBT serializeNBT(CompoundNBT nbt) {
		nbt.put("planets", planets.serializeNBT());
		return nbt;
	}

	// Create new planet data per tag
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		planets.deserializeNBT(nbt.getCompound("planets"));
	}
	
	/**
	 * Gets the current data of the dimension or creates it if not present. <br>
	 * {@link Optional} because it can still <i>somehow</i> be null after being created
	 * @param dimID
	 * @return
	 */
	public Optional<PlanetData> getDimensionData(ResourceLocation dimID) {
		return Optional.ofNullable(planets.get(dimID));
	}
	
	public Optional<PlanetData> getDimensionDataOrCreate(ResourceLocation dimID) {
		planets.computeIfAbsent(dimID, key -> new PlanetData());
		return Optional.ofNullable(planets.get(dimID));
	}
}
