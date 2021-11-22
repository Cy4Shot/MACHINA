package com.cy4.machina.compat.jei.trait;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientHelper;

public class PlanetTraitJEIHelper implements IIngredientHelper<PlanetTrait> {

	@Override
	public PlanetTrait getMatch(Iterable<PlanetTrait> traits, PlanetTrait traitToMatch) {
		return Lists.newArrayList(traits).stream().filter(trait -> traitToMatch == trait).findFirst().get();
	}

	@Override
	public String getDisplayName(PlanetTrait trait) {
		return trait.toString();
	}

	@Override
	public String getUniqueId(PlanetTrait trait) {
		return trait.getRegistryName().toString();
	}

	@Override
	public String getModId(PlanetTrait trait) {
		return trait.getRegistryName().getNamespace();
	}

	@Override
	public String getResourceId(PlanetTrait trait) {
		return trait.getRegistryName().getPath();
	}

	@Override
	public PlanetTrait copyIngredient(PlanetTrait trait) {
		return trait;
	}

	@Override
	public String getErrorInfo(PlanetTrait trait) {
		return trait.toString();
	}

	@Override
	public Iterable<Integer> getColors(PlanetTrait trait) {
		return Lists.newArrayList(trait.getColor());
	}
}
