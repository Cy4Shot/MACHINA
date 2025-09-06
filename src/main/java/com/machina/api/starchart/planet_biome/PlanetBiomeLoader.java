package com.machina.api.starchart.planet_biome;

import com.machina.api.util.loader.JsonLoader;

public class PlanetBiomeLoader extends JsonLoader<PlanetBiomeSettings> {

    public static final PlanetBiomeLoader INSTANCE = new PlanetBiomeLoader();

    public PlanetBiomeLoader() {
        super("planet_biome", PlanetBiomeJsonInfo.class);
    }
}