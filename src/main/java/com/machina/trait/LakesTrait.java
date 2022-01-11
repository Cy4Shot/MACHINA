package com.machina.trait;

import com.machina.api.planet.trait.PlanetTrait;
import com.machina.api.planet.trait.type.IWorldTrait;

public class LakesTrait extends PlanetTrait implements IWorldTrait {

	public LakesTrait(int color, String description) {
		super(color, description);
	}

}
