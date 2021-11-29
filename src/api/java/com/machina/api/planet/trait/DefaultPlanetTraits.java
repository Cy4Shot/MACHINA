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

package com.machina.api.planet.trait;

import com.machina.api.util.MachinaRL;

public class DefaultPlanetTraits {
	
	public static final PlanetTrait NOT_EXISTING = PlanetTrait.REGISTRY.getValue(new MachinaRL("not_existing"));
	public static final PlanetTrait WATER_WORLD = get("water_world");
	public static final PlanetTrait CONTINENTALL = get("continental");
	public static final PlanetTrait LANDMMASS = get("landmass");
	public static final PlanetTrait MOUNTAINOUS = get("mountainous");
	public static final PlanetTrait HILLY = get("hilly");
	public static final PlanetTrait FLAT = get("flat");
	public static final PlanetTrait FROZEN = get("frozen");
	
	public static PlanetTrait get(String name) {
		return PlanetTrait.REGISTRY.getValue(new MachinaRL(name));
	}
}
