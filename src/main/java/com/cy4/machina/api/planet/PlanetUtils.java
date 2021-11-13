package com.cy4.machina.api.planet;

import com.cy4.machina.Machina;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class PlanetUtils {

	public static boolean isDimensionPlanet(RegistryKey<World> dim) {
		return dim.location().getNamespace().equals(Machina.MOD_ID);
	}

}
