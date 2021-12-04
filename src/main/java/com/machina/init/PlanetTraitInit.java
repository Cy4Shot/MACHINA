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
import com.machina.api.planet.trait.PlanetTrait.JeiProperties;
import com.machina.api.planet.trait.PlanetTrait.Properties;
import com.machina.api.registry.annotation.RegisterPlanetTrait;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;

import net.minecraft.item.Items;

import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Cy4Shot
 */
@SuppressWarnings("deprecation")
@RegistryHolder(modid = MACHINA)
public final class PlanetTraitInit {

	@RegisterPlanetTrait(id = "water_world")
	public static final PlanetTrait WATER_WORLD = new PlanetTrait(0x169fde);

	@RegisterPlanetTrait(id = "continental")
	public static final PlanetTrait CONTINENTALL = new PlanetTrait(0x24bf2a);

	@RegisterPlanetTrait(id = "landmass")
	public static final PlanetTrait LANDMMASS = new PlanetTrait(0xada39a);

	@RegisterPlanetTrait(id = "mountainous")
	public static final PlanetTrait MOUNTAINOUS = new PlanetTrait(0x807f7e);

	@RegisterPlanetTrait(id = "hilly")
	public static final PlanetTrait HILLY = new PlanetTrait(0x613407);

	@RegisterPlanetTrait(id = "flat")
	public static final PlanetTrait FLAT = new PlanetTrait(0x449e41);

	@RegisterPlanetTrait(id = "ore_rich")
	public static final PlanetTrait ORE_RICH = new PlanetTrait(0x24f0db);

	@RegisterPlanetTrait(id = "ore_barren")
	public static final PlanetTrait ORE_BARREN = new PlanetTrait(0x0c0d0d);

	@RegisterPlanetTrait(id = "canyons")
	public static final PlanetTrait CANYONS = new PlanetTrait(0x9dbfbf);

	@RegisterPlanetTrait(id = "fiords")
	public static final PlanetTrait FIORDS = new PlanetTrait(0x3f5be8);

	@RegisterPlanetTrait(id = "ravines")
	public static final PlanetTrait RAVINES = new PlanetTrait(0x121626);

	@RegisterPlanetTrait(id = "lakes")
	public static final PlanetTrait LAKES = new PlanetTrait(0x098aed);

	@RegisterPlanetTrait(id = "volcanic")
	public static final PlanetTrait VOLCANIC = new PlanetTrait(0xf54d0a);

	@RegisterPlanetTrait(id = "frozen")
	public static final PlanetTrait FROZEN = new PlanetTrait(0x0aabf5);

	@RegisterPlanetTrait(id = "layered")
	public static final PlanetTrait LAYERED = new PlanetTrait(0x36870b);

	/**
	 * This is similar to {@link Items#AIR}. It is an empty trait that will be the
	 * default value of
	 * {@link IForgeRegistry#getValue(net.minecraft.util.ResourceLocation)}. <br>
	 * <strong>DO NOT ADD TO A PLANET</strong>
	 */
	@RegisterPlanetTrait(id = "not_existing")
	public static final PlanetTrait NOT_EXISTING = new PlanetTrait(
			new Properties(0x000000).withJeiProperties(new JeiProperties().setShowsInJei(false)));

	@RegistryHolder(modid = MACHINA)
	public static final class Environmental {

		@RegisterPlanetTrait(id = "supercold")
		public static final PlanetTrait SUPERCOLD = new PlanetTrait(0xffffff);
	}
	
	@RegisterPlanetTrait(id = "low_gravity")
	public static final PlanetTrait LOW_GRAVITY = new PlanetTrait(new Properties(0x000000)) {
		@Override
		public boolean hasAttributeModifier() { return true; }
		@Override
		public void addAttribute(com.machina.api.planet.attribute.PlanetAttributeList attributes) {
			attributes.setValue(PlanetAttributeTypesInit.GRAVITY, oldVal -> {
				if (oldVal != null) {
					return oldVal / 2;
				}
				return 4.9;
			});
		}
		@Override
		public void removeAttribute(com.machina.api.planet.attribute.PlanetAttributeList attributes) {
			attributes.setValue(PlanetAttributeTypesInit.GRAVITY, oldVal -> {
				if (oldVal != null) {
					return oldVal * 2;
				}
				return 9.8;
			});
		}
	};
}
