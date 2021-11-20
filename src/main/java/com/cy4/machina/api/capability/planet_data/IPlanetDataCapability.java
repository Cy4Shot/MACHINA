package com.cy4.machina.api.capability.planet_data;

import java.util.List;

import com.cy4.machina.api.planet.trait.PlanetTrait;

public interface IPlanetDataCapability {

	void addTrait(PlanetTrait trait);

	void removeTrait(PlanetTrait trait);

	List<PlanetTrait> getTraits();

}
