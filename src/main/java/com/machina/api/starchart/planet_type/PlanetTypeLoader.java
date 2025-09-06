package com.machina.api.starchart.planet_type;

import java.util.Random;
import java.util.Set;

import com.machina.api.util.loader.JsonLoader;

import net.minecraft.resources.ResourceLocation;

public class PlanetTypeLoader extends JsonLoader<PlanetType> {

    public static final PlanetTypeLoader INSTANCE = new PlanetTypeLoader();

    public PlanetTypeLoader() {
        super("planet_type", PlanetTypeJsonInfo.class);
    }

    public ResourceLocation pickRandom(Random random) {
        Set<ResourceLocation> set = getAllLoc();
        ResourceLocation[] all = set.toArray(new ResourceLocation[0]);
        return all[random.nextInt(all.length)];
    }
}