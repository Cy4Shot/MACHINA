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

package com.machina.init;

import static com.machina.api.ModIDs.MACHINA;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.registry.annotation.RegisterPlanetTrait;
import com.machina.trait.FrozenTrait;
import com.machina.trait.HeightMultiplierTrait;
import com.machina.trait.WaterHeightTrait;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

/**
 * @author Cy4Shot
 */
@RegistryHolder(modid = MACHINA)
public final class PlanetTraitInit {

	@RegisterPlanetTrait(id = "water_world")
	public static final PlanetTrait WATER_WORLD = new WaterHeightTrait(0x169fde, "", 70);

	@RegisterPlanetTrait(id = "continental")
	public static final PlanetTrait CONTINENTALL = new WaterHeightTrait(0x24bf2a, "", 60);

	@RegisterPlanetTrait(id = "landmass")
	public static final PlanetTrait LANDMMASS = new WaterHeightTrait(0xada39a, "", -1);

	@RegisterPlanetTrait(id = "mountainous")
	public static final PlanetTrait MOUNTAINOUS = new HeightMultiplierTrait(0x807f7e, "", 7.0f);

	@RegisterPlanetTrait(id = "hilly")
	public static final PlanetTrait HILLY = new HeightMultiplierTrait(0x613407, "", 3.5f);

	@RegisterPlanetTrait(id = "flat")
	public static final PlanetTrait FLAT = new HeightMultiplierTrait(0x449e41, "", 1.3f);

	@RegisterPlanetTrait(id = "lakes")
	public static final PlanetTrait LAKES = new PlanetTrait(0x098aed, "");

	@RegisterPlanetTrait(id = "frozen")
	public static final PlanetTrait FROZEN = new FrozenTrait(0x0aabf5, "");
}
