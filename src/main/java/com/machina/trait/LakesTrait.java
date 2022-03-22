package com.machina.trait;

import com.machina.planet.trait.PlanetTrait;
import com.machina.planet.trait.type.IWorldTrait;

public class LakesTrait extends PlanetTrait implements IWorldTrait {

	public LakesTrait(int color) {
		super(color);
	}

}
