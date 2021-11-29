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

package com.machina.datagen.common;

import static com.machina.init.PlanetTraitInit.*;

import com.machina.Machina;
import com.machina.api.datagen.trait_pools.TraitPool;
import com.machina.api.datagen.trait_pools.TraitPoolEntry;
import com.machina.api.datagen.trait_pools.TraitPoolsGenProvider;
import com.machina.init.PlanetTraitInit;

import net.minecraft.data.DataGenerator;

public class PlanetTraitPoolsProvider extends TraitPoolsGenProvider {

	public PlanetTraitPoolsProvider(DataGenerator generator) {
		super(generator, Machina.MOD_ID);
	}

	@Override
	protected void addPools() {
		addPool("geographical_traits", new TraitPool(3, 5).withEntries(
				new TraitPoolEntry(15, PlanetTraitInit.CONTINENTALL, PlanetTraitInit.WATER_WORLD,
						PlanetTraitInit.LANDMMASS),
				new TraitPoolEntry(15, PlanetTraitInit.MOUNTAINOUS, PlanetTraitInit.HILLY, PlanetTraitInit.FLAT),
				new TraitPoolEntry(15, ORE_BARREN, ORE_RICH), new TraitPoolEntry(5, CANYONS, FIORDS, RAVINES, LAKES),
				new TraitPoolEntry(5, VOLCANIC, FROZEN), new TraitPoolEntry(2, LAYERED)));
	}

}
