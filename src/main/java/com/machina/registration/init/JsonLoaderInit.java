package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;

import com.machina.api.multiblock.MultiblockLoader;
import com.machina.api.starchart.planet_biome.PlanetBiomeLoader;
import com.machina.api.starchart.planet_structure.PlanetStructureLoader;
import com.machina.api.starchart.planet_type.PlanetTypeLoader;
import com.machina.api.util.loader.JsonLoader;

import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class JsonLoaderInit {

	private static final List<JsonLoader<?>> LOADERS = new ArrayList<>();

	static {
		LOADERS.add(MultiblockLoader.INSTANCE);
		LOADERS.add(PlanetTypeLoader.INSTANCE);
		LOADERS.add(PlanetBiomeLoader.INSTANCE);
		LOADERS.add(PlanetStructureLoader.INSTANCE);
	}

	public static void registerAll(final AddReloadListenerEvent e) {
		LOADERS.forEach(e::addListener);
	}
}