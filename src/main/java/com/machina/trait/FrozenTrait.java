package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;

public class FrozenTrait extends PlanetTrait implements IWorldTrait{

	public FrozenTrait(int color) {
		super(color);
	}

}
