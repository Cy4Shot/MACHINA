/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

package com.machina.compat.jei.trait;

import com.google.common.collect.Lists;
import com.machina.api.planet.trait.PlanetTrait;

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
