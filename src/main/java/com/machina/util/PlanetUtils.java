package com.machina.util;

import com.machina.Machina;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;

public class PlanetUtils {

	public static boolean isDimensionPlanet(RegistryKey<World> dim) {
		return dim.location().getNamespace().equals(Machina.MOD_ID);
	}

	public static int getId(RegistryKey<World> dim) {
		return Integer.valueOf(dim.location().getPath());
	}

	public static int getIdDim(RegistryKey<Dimension> dim) {
		return Integer.valueOf(dim.location().getPath());
	}

}
