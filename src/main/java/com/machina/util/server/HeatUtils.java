package com.machina.util.server;

import com.machina.config.CommonConfig;
import com.machina.registration.init.AttributeInit;
import com.machina.world.data.StarchartData;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class HeatUtils {

	public static int getRange() {
		return CommonConfig.maxHeat.get();
	}

	public static float propFull(float heat, RegistryKey<World> dim){
		return normalizeHeat(heat, dim) / (float) getRange();
	}

	public static float normalizeHeat(float heat, RegistryKey<World> dim) {
		if (PlanetUtils.isDimensionPlanet(dim)) {
			return StarchartData.getDataForDimension(ServerHelper.server(), dim).getAttribute(AttributeInit.TEMPERATURE)
					+ heat;
		} else {
			return AttributeInit.TEMPERATURE.ser.def() + heat;
		}
	}

}
