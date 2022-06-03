package com.machina.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.machina.util.MachinaRL;
import com.machina.util.server.PlanetUtils;
import com.machina.world.data.PlanetData;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ClientStarchart {
	private static Map<ResourceLocation, PlanetData> starchart;

	public static Map<ResourceLocation, PlanetData> getStarchart() {
		return starchart;
	}

	public static void setStarchart(Map<ResourceLocation, PlanetData> starchart) {
		ClientStarchart.starchart = starchart;
	}

	public static PlanetData getPlanetData(int id) {
		return getDataForDimension(new MachinaRL(id));
	}
	
	public static PlanetData getPlanetData(RegistryKey<World> dim) {
		return getPlanetData(PlanetUtils.getId(dim));
	}

	public static PlanetData getDataForDimension(ResourceLocation dimID) {
		return starchart.computeIfAbsent(dimID, rl -> PlanetData.NONE);
	}

	public static List<PlanetData> planets() {
		return new ArrayList<PlanetData>(starchart.values());
	}
}
