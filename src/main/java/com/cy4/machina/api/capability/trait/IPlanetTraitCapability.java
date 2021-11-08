package com.cy4.machina.api.capability.trait;

import java.util.List;

import com.cy4.machina.api.planet.PlanetTrait;

public interface IPlanetTraitCapability {
	
	void addTrait(PlanetTrait trait);
	
	void removeTrait(PlanetTrait trait);
	
	List<PlanetTrait> getTraits();

}
