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

package com.cy4.machina.starchart;

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
import com.cy4.machina.api.util.Color;
import com.cy4.machina.init.PlanetAttributeTypesInit;

import net.minecraft.nbt.CompoundNBT;

import net.minecraftforge.common.util.INBTSerializable;

public class PlanetData implements INBTSerializable<CompoundNBT> {

	private final PlanetTraitList traits;
	private final PlanetAttributeList attributes = new PlanetAttributeList();
	
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
		attributes.setValue(PlanetAttributeTypesInit.PLANET_NAME, oldval -> PlanetNameGenerator.getName(rand));
		float r = rand.nextFloat() / 2f;
		float g = rand.nextFloat() / 2f;
		float b = rand.nextFloat() / 2f;
		attributes.setValue(PlanetAttributeTypesInit.COLOUR, oldval -> new Color(r, g, b));
		
		traits.clear();
		traits.addAll(getTraits(rand));

		attributes.setValue(PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE, oldVal -> rand.nextFloat());
		attributes.setValue(PlanetAttributeTypesInit.TEMPERATURE, oldVal -> rand.nextFloat());
		dist = rand.nextFloat();

		// TODO: Apply trait modifiers
		/** trait modifiers should be applied using {@link PlanetTrait#addAttribute} */
	}

	public PlanetData() {
		this.traits = new PlanetTraitList(attributes);
	}
	
	public String getName() {
		return attributes.getAttributeForType(PlanetAttributeTypesInit.PLANET_NAME).isPresent() ? 
				attributes.getAttributeForType(PlanetAttributeTypesInit.PLANET_NAME).get().getValue() : "Planet";
	}
	
	public Color getColour() {
		return attributes.getAttributeForType(PlanetAttributeTypesInit.COLOUR).isPresent() ? 
				attributes.getAttributeForType(PlanetAttributeTypesInit.COLOUR).get().getValue() : new Color(0);
	}
	
	public float getAtmosphericPressure() {
		return attributes.getAttributeForType(PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE).isPresent() ? 
				attributes.getAttributeForType(PlanetAttributeTypesInit.ATMOSPHERIC_PRESSURE).get().getValue() : 1.0f;
	}
	
	public float getTemperature() {
		return attributes.getAttributeForType(PlanetAttributeTypesInit.TEMPERATURE).isPresent() ? 
				attributes.getAttributeForType(PlanetAttributeTypesInit.TEMPERATURE).get().getValue() : 1.0f;
	}

	// Serialize all data
	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();
		nbt.put("traits", traits.serializeNBT());
		nbt.put("attributes", attributes.serializeNBT());
		
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
		data.dist = nbt.getFloat("dist");
		return data;
	}

	public String getAtm() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(getAtmosphericPressure() * 100F + 50F) + "kPa";
	}

	public String getTemp() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(getTemperature() * 200F + 200F) + "K";
	}

	public String getDist() {
		DecimalFormat df = new DecimalFormat("##");
		return df.format(dist * 1000F + 100F) + "Gm";
	}
	
	public PlanetTraitList getTraits() { return traits; }
	public PlanetAttributeList getAttributes() { return attributes; }

}
