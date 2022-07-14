package com.machina.planet.trait;

import com.machina.registration.init.TraitInit;
import com.machina.util.text.StringUtils;

import net.minecraftforge.registries.ForgeRegistryEntry;

public class PlanetTrait extends ForgeRegistryEntry<PlanetTrait> {

	private final int color;

	public PlanetTrait(int color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return StringUtils.translate(getRegistryName().getNamespace() + ".planet_trait." + getRegistryName().getPath());
	}

	public boolean exists() {
		return this != TraitInit.NONE;
	}

	public int getColor() {
		return color;
	}
}
