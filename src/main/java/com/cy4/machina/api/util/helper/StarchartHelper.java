/*
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.api.util.helper;

import com.cy4.machina.api.planet.trait.PlanetTrait;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.world.data.StarchartData;

import net.minecraft.world.World;

public class StarchartHelper {

	public static void addTraitToWorld(World level, PlanetTrait trait) {
		Starchart serverChart = StarchartData.getStarchartForServer(level.getServer());
		serverChart.getDimensionDataOrCreate(level.dimension().location()).ifPresent(data -> data.getTraits().addTrait(trait));
		setDirtyAndUpdate(level);
	}
	
	public static void removeTraitFromWorld(World level, PlanetTrait trait) {
		Starchart serverChart = StarchartData.getStarchartForServer(level.getServer());
		serverChart.getDimensionDataOrCreate(level.dimension().location()).ifPresent(data -> data.getTraits().removeTrait(trait));
		setDirtyAndUpdate(level);
	}
	
	public static void setDirtyAndUpdate(World level) {
		StarchartData.getDefaultInstance(level.getServer()).setDirty();
		StarchartData.getDefaultInstance(level.getServer()).syncClients();
	}

}
