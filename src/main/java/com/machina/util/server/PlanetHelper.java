package com.machina.util.server;

import com.machina.Machina;
import com.machina.config.CommonConfig;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.Dimension;
import net.minecraft.world.World;

public class PlanetHelper {

	public static boolean isDimensionPlanet(RegistryKey<World> dim) {
		return dim.location().getNamespace().equals(Machina.MOD_ID);
	}

	public static int getId(RegistryKey<World> dim) {
		return Integer.valueOf(dim.location().getPath());
	}

	public static int getIdDim(RegistryKey<Dimension> dim) {
		return Integer.valueOf(dim.location().getPath());
	}

	public static double getAtmosphereChemical(PlanetData data, FluidObject chemical, RegistryKey<World> dim) {
		return data.getAtmosphere(dim)[FluidInit.ATMOSPHERE.indexOf(chemical)];
	}

	public static boolean canBreath(RegistryKey<World> dim) {
		if (!isDimensionPlanet(dim))
			return true;
		return getAtmosphereChemical(StarchartData.getDataForDimension(dim), FluidInit.OXYGEN,
				dim) > CommonConfig.minAtmOxygen.get();
	}

}
