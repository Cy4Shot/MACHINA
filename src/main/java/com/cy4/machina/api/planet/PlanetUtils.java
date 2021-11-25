/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.planet;

import com.cy4.machina.Machina;

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
