package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterPlanetTrait;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.api.planet.PlanetTrait;

/**
 * @author Cy4Shot
 */

@RegistryHolder(modid = "machina")
public class PlanetTraitInit {

	@RegisterPlanetTrait(id = "example")
	public static final PlanetTrait EXAMPLE = new PlanetTrait("TESTING TESTING");
}
