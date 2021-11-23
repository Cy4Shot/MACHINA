package com.cy4.machina.starchart;

import java.util.Random;

import com.cy4.machina.api.nbt.NbtList;
import com.cy4.machina.config.CommonConfig;
import com.cy4.machina.util.StringUtils;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class Starchart implements INBTSerializable<CompoundNBT> {

	public NbtList<PlanetData> planets;

	public Starchart() {
		planets = new NbtList<>(PlanetData::deserialize);
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
			planets.add(new PlanetData(rand));
		}
	}

	public void debugStarchart() {
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < planets.size(); i++) {
			PlanetData p = planets.get(i);
			StringUtils.printlnUtf8(
					(i == planets.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H + p.name);
			for (int j = 0; j < p.traits.size(); j++) {
				StringUtils.printlnUtf8((i == planets.size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.traits.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.traits.get(j).toString());
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
		planets.clear();
		planets.deserializeNBT(nbt.getCompound("planets"));
	}
}
