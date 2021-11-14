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
	public static final PlanetTrait WATER_WORLD = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "continental")
	public static final PlanetTrait CONTINENTALL = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "landmass")
	public static final PlanetTrait LANDMMASS = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "mountainous")
	public static final PlanetTrait MOUNTAINOUS = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "hilly")
	public static final PlanetTrait HILLY = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "flat")
	public static final PlanetTrait FLAT = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "ore_rich")
	public static final PlanetTrait ORE_RICH = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "ore_barren")
	public static final PlanetTrait ORE_BARREN = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "canyons")
	public static final PlanetTrait CANYONS = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "fiords")
	public static final PlanetTrait FIORDS = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "ravines")
	public static final PlanetTrait RAVINES = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "lakes")
	public static final PlanetTrait LAKES = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "volcanic")
	public static final PlanetTrait VOLCANIC = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "frozen")
	public static final PlanetTrait FROZEN = new PlanetTrait(0xFFFFFF);

	@RegisterPlanetTrait(id = "layered")
	public static final PlanetTrait LAYERED = new PlanetTrait(0xFFFFFF);
}
