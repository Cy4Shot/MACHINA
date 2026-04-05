package com.machina.api.starchart.planet_trait;

import com.machina.registration.init.BlockInit.MachinaOre;

public class PlanetOreTrait extends PlanetTrait {
	
	private final MachinaOre ore;
	
	public PlanetOreTrait(String name, int color, MachinaOre ore) {
		super(name, color);
		this.ore = ore;
	}
}
