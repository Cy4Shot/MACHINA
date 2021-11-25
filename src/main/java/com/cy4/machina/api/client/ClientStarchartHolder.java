package com.cy4.machina.api.client;

import com.cy4.machina.starchart.PlanetData;
import com.cy4.machina.starchart.Starchart;
import com.cy4.machina.util.MachinaRL;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientStarchartHolder {

	private static Starchart starchart;

	public static Starchart getStarchart() { return starchart; }

	public static void setStarchart(Starchart starchart) { ClientStarchartHolder.starchart = starchart; }
	
	public static PlanetData getPlanetDataByID(int id) {
		return starchart.planets.get(new MachinaRL(id));
	}
	
	public static java.util.Optional<PlanetData> getDataForDimension(ResourceLocation dimID) {
		return starchart.getDimensionData(dimID);
	}
}
