package com.machina.registration.init;

import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.api.starchart.planet_type.PlanetTypeLoader;
import com.machina.api.util.loader.JsonLoader;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.ArrayList;
import java.util.List;

public class JsonLoaderInit {

    private static final List<JsonLoader<?>> LOADERS = new ArrayList<>();

    static {
        LOADERS.add(MultiblockLoader.INSTANCE);
        LOADERS.add(PlanetTypeLoader.INSTANCE);
        LOADERS.add(PlanetBiomeLoader.INSTANCE);
    }

    public static void registerAll(final AddReloadListenerEvent e) {
        LOADERS.forEach(e::addListener);
    }
}