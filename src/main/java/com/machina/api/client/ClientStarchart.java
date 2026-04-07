package com.machina.api.client;

import com.machina.api.starchart.Starchart;
import com.machina.api.starchart.StarchartGenerator;
import com.machina.api.starchart.obj.Planet;
import com.machina.api.starchart.obj.SolarSystem;
import com.machina.api.util.PlanetHelper;

import net.minecraft.client.multiplayer.ClientLevel;

public class ClientStarchart {
	public static SolarSystem system;

	public static Starchart STARCHART = null;
	public static long SEED = 0L;

	public static void sync(long seed) {
		system = StarchartGenerator.gen(seed);
		SEED = seed;
	}
	
	public static Planet getPlanet(ClientLevel level) {
		int id = PlanetHelper.getIdLevel(level.dimension());
		if (system == null) {
			return null;
		}
		return system.planets().get(id);
	}
}
