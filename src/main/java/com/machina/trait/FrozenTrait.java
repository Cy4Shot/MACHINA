package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;

public class FrozenTrait extends PlanetTrait implements IWorldTrait{

	public FrozenTrait(int color, String description) {
		super(color, description);
	}

}
