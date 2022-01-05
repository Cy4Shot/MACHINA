package com.machina.api.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.machina.api.util.MachinaRL;
import com.machina.api.world.data.PlanetData;

import net.minecraft.util.ResourceLocation;

public class ClientDataHolder {
	private static Map<ResourceLocation, PlanetData> starchart;

	public static  Map<ResourceLocation, PlanetData> getStarchart() { return starchart; }

	public static void setStarchart( Map<ResourceLocation, PlanetData> starchart) { ClientDataHolder.starchart = starchart; }
	
	public static PlanetData getPlanetDataByID(int id) {
		return getDataForDimension(new MachinaRL(id));
	}
	
	public static PlanetData getDataForDimension(ResourceLocation dimID) {
		return starchart.computeIfAbsent(dimID, rl -> PlanetData.NONE);
	}
	
	public static List<PlanetData> planets() {
		return new ArrayList<PlanetData>(starchart.values());
	}
}
