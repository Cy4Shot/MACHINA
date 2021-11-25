/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.compat.jei;

import com.cy4.machina.api.planet.trait.PlanetTrait;

import mezz.jei.api.ingredients.IIngredientType;

public final class MachinaJEITypes {

	public static final IIngredientType<PlanetTrait> PLANET_TRAIT = () -> PlanetTrait.class;

	private MachinaJEITypes() {
	}

}
