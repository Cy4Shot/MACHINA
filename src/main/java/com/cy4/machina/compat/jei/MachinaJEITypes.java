package com.cy4.machina.compat.jei;

import com.cy4.machina.api.planet.trait.PlanetTrait;

import mezz.jei.api.ingredients.IIngredientType;

public final class MachinaJEITypes {

	public static final IIngredientType<PlanetTrait> PLANET_TRAIT = () -> PlanetTrait.class;
	
	private MachinaJEITypes() {
	}
	
}
