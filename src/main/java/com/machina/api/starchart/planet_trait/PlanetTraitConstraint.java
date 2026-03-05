package com.machina.api.starchart.planet_trait;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlanetTraitConstraint {
	Set<PlanetTrait> traits;

	public PlanetTraitConstraint(PlanetTrait... traits) {
		this(new HashSet<>(Arrays.asList(traits)));
	}
	
	public PlanetTraitConstraint(Set<PlanetTrait> traits) {
		this.traits = traits;
	}

	public boolean violates(Set<PlanetTrait> selectedTraits) {
		int count = 0;
		for (PlanetTrait trait : traits) {
			if (selectedTraits.contains(trait))
				count++;
		}
		return count > 1;
	}
}
