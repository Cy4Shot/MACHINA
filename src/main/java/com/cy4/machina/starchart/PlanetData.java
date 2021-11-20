package com.cy4.machina.starchart;

import java.awt.Color;
import java.text.DecimalFormat;
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
import net.minecraft.world.storage.WorldSavedData;

import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @deprecated
 * Please use the capability. Now that you can sync and get client data easily, theres no
 * point in a {@link WorldSavedData}. Ask in any modding community: If I have the option of
 * either using a WSD or a World capability (being able to do what i want in both cases)
 * what should i use? (spoilers: the answer is a capability, because the capabilities 
 * are put in a WSD at one point automatically and because they allow for very easy mod
 * compatibility)
 */
@Deprecated
public class PlanetData implements INBTSerializable<CompoundNBT> {
	public List<PlanetTrait> traits = new ArrayList<>();
	public String name = "Planet";
	public int color;

	public float atm; // Atmospheric Pressure
	public float temp; // Temperature
	public float dist; // Distance from terra prime

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
		float r = rand.nextFloat() / 2f;
		float g = rand.nextFloat() / 2f;
		float b = rand.nextFloat() / 2f;
		color = new Color(r, g, b).getRGB();
		traits = getTraits(rand);

		atm = rand.nextFloat();
		temp = rand.nextFloat();
		dist = rand.nextFloat();

		// TODO: Apply trait modifiers
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

	public String getAtm() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(atm * 100F + 50F) +"kPa";
	}

	public String getTemp() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(temp * 200F + 200F) + "K";
	}

	public String getDist() {
		DecimalFormat df = new DecimalFormat("##");
		return df.format(dist * 1000F + 100F) + "Gm";
	}

}
