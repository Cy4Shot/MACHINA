package com.machina.api.starchart.planet_structure;

import com.machina.api.util.loader.JsonLoader;

public class PlanetStructureLoader extends JsonLoader<PlanetStructure> {

	public static final PlanetStructureLoader INSTANCE = new PlanetStructureLoader();

	public PlanetStructureLoader() {
		super("planet_structure", PlanetStructureJsonInfo.class);
	}
}
