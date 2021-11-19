package com.cy4.machina.init;

import com.cy4.machina.Machina;
import com.cy4.machina.api.annotation.registries.RegisterPlanetTrait;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.planet.trait.PlanetTrait;

/**
 * @author Cy4Shot
 */

@RegistryHolder(modid = Machina.MOD_ID)
public final class PlanetTraitInit  {

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
}
