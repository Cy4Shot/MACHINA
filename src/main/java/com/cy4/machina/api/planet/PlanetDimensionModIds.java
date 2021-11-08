package com.cy4.machina.api.planet;

import java.util.LinkedList;
import java.util.List;

import com.cy4.machina.Machina;
import com.google.common.collect.Lists;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class PlanetDimensionModIds {
	
	private static List<String> modids = Lists.newArrayList(Machina.MOD_ID);
	
	public static void addModId(String id) {
		modids.add(id);
	}
	
	public static List<String> getModIds() {
		List<String> copy = new LinkedList<>();
		modids.forEach(copy::add);
		return copy;
	}
	
	public static boolean isDimensionPlanet(RegistryKey<World> dim) {
		return modids.contains(dim.location().getNamespace());
	}

}
