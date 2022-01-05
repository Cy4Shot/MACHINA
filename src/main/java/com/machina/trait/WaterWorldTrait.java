package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;

public class WaterWorldTrait extends PlanetTrait implements IWorldTrait {

	public WaterWorldTrait(int color, String description) {
		super(color, description);
	}

}
