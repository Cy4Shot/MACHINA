package com.cy4.machina.compat.jei.trait;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredientHelper;

public class PlanetTraitJEIHelper implements IIngredientHelper<PlanetTrait> {

	@Override
	public PlanetTrait getMatch(Iterable<PlanetTrait> ingredients, PlanetTrait ingredientToMatch) {
		return Lists.newArrayList(ingredients).stream().filter(trait -> ingredientToMatch == trait).findFirst().get();
	}

	@Override
	public String getDisplayName(PlanetTrait ingredient) {
		return ingredient.toString();
	}

	@Override
	public String getUniqueId(PlanetTrait ingredient) {
		return ingredient.getRegistryName().toString();
	}

	@Override
	public String getModId(PlanetTrait ingredient) {
		return ingredient.getRegistryName().getNamespace();
	}

	@Override
	public String getResourceId(PlanetTrait ingredient) {
		return ingredient.getRegistryName().getPath();
	}

	@Override
	public PlanetTrait copyIngredient(PlanetTrait ingredient) {
		return ingredient;
	}

	@Override
	public String getErrorInfo(PlanetTrait ingredient) {
		return "";
	}

}
