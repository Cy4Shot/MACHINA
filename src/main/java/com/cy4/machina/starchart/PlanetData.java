package com.cy4.machina.starchart;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetNameGenerator;
import com.cy4.machina.api.planet.attribute.PlanetAttributeList;
import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.api.planet.trait.PlanetTraitList;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {

	private final PlanetTraitList traits = new PlanetTraitList();
	private final PlanetAttributeList attributes = new PlanetAttributeList();
	
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
		
		traits.clear();
		traits.addAll(getTraits(rand));

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
		nbt.put("traits", traits.serializeNBT());
		nbt.put("attributes", attributes.serializeNBT());

		nbt.putString("name", name);
		nbt.putFloat("atm", atm);
		nbt.putFloat("temp", temp);
		nbt.putFloat("dist", dist);

		return nbt;
	}

	// Read all data
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		deserialize(nbt, this);
	}

	public static PlanetData deserialize(CompoundNBT nbt) {
		PlanetData data = new PlanetData();
		deserialize(nbt, data);
		return data;
	}

	private static PlanetData deserialize(CompoundNBT nbt, PlanetData data) {
		data.traits.deserializeNBT(nbt.getCompound("traits"));
		data.attributes.deserializeNBT(nbt.getCompound("attributes"));
		data.name = nbt.getString("name");
		data.atm = nbt.getFloat("atm");
		data.temp = nbt.getFloat("temp");
		data.dist = nbt.getFloat("dist");
		return data;
	}

	public String getAtm() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(atm * 100F + 50F) + "kPa";
	}

	public String getTemp() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(temp * 200F + 200F) + "K";
	}

	public String getDist() {
		DecimalFormat df = new DecimalFormat("##");
		return df.format(dist * 1000F + 100F) + "Gm";
	}
	
	public PlanetTraitList getTraits() { return traits; }
	public PlanetAttributeList getAttributes() { return attributes; }

}
