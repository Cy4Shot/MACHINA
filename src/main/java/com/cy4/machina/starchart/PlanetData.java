package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetNameGenerator;
import com.cy4.machina.api.planet.trait.PlanetTrait;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {
	public List<PlanetTrait> traits = new ArrayList<>();
	public String name = "Planet";

	public static List<PlanetTrait> getTraits(Random rand) {
		List<PlanetTrait> res = new ArrayList<>();
		Machina.traitPoolManager.forEach((location, pool) -> res
				.addAll(pool.roll(rand).stream().map(PlanetTrait.REGISTRY::getValue).collect(Collectors.toList())));
		return res;
	}

	public static PlanetData fromNBT(CompoundNBT nbt) {
		PlanetData data = new PlanetData();
		data.deserializeNBT(nbt);
		return data;
	}

	public PlanetData(Random rand) {
		this();
		name = PlanetNameGenerator.getName(rand);
		traits = getTraits(rand);
	}

	public PlanetData() {
	}

	// Serialize all data
	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();
		ListNBT t = new ListNBT();
		t.addAll(traits.stream().map(data -> StringNBT.valueOf(data.getRegistryName().toString()))
				.collect(Collectors.toList()));
		nbt.put("traits", t);

		nbt.putString("name", name);

		return nbt;
	}

	// Read all data
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		traits.clear();
		nbt.getList("traits", Constants.NBT.TAG_STRING).forEach(val -> traits
				.add(PlanetTrait.REGISTRY.getValue(new ResourceLocation(((StringNBT) val).getAsString()))));

		name = nbt.getString("name");
	}
}
