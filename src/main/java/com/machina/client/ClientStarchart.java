package com.machina.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.machina.util.server.PlanetHelper;
import com.machina.util.text.MachinaRL;
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
		return getPlanetData(PlanetHelper.getId(dim));
	}

	public static PlanetData getDataForDimension(ResourceLocation dimID) {
		return starchart.computeIfAbsent(dimID, rl -> PlanetData.NONE);
	}

	public static List<PlanetData> planets() {
		return new ArrayList<PlanetData>(starchart.values());
	}

	public static ResourceLocation getId(PlanetData value) {
		for (Map.Entry<ResourceLocation, PlanetData> entry : starchart.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
