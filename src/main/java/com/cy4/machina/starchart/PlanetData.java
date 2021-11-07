package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.cy4.machina.api.planet.PlanetTrait;
import com.cy4.machina.util.json.PlanetTraitPool;
import com.google.gson.Gson;

import net.minecraft.util.ResourceLocation;

public class PlanetData {
	public List<PlanetTrait> TRAITS = new ArrayList<>();

	public static List<PlanetTrait> getTraits(long seed) {
		Gson gson = new Gson();
		PlanetTraitPool pool = gson.fromJson("{\r\n"
				+ "	\"minRolls\": 3,\r\n"
				+ "	\"maxRolls\": 5,\r\n"
				+ "	\"entries\": [\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 15,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:water_world\",\r\n"
				+ "				\"machina:continental\",\r\n"
				+ "				\"machina:landmass\"\r\n"
				+ "			]\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 15,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:mountainous\",\r\n"
				+ "				\"machina:hilly\",\r\n"
				+ "				\"machina:flat\"\r\n"
				+ "			]\r\n"
				+ "		\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 15,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:ore_rich\",\r\n"
				+ "				\"machina:ore_barren\"\r\n"
				+ "			]\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 5,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:canyons\",\r\n"
				+ "				\"machina:fiords\",\r\n"
				+ "				\"machina:ravines\",\r\n"
				+ "				\"machina:lakes\"\r\n"
				+ "			]\r\n"
				+ "		\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 5,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:volcanic\",\r\n"
				+ "				\"machina:frozen\"\r\n"
				+ "			]\r\n"
				+ "		},\r\n"
				+ "		{\r\n"
				+ "			\"weight\": 2,\r\n"
				+ "			\"values\": [\r\n"
				+ "				\"machina:layered\"\r\n"
				+ "			]\r\n"
				+ "		}\r\n"
				+ "	]\r\n"
				+ "}\r\n"
				+ "", PlanetTraitPool.class);
		
		System.out.println("MaxRolls " + String.valueOf(pool.maxRolls));
		System.out.println("MinRolls " + String.valueOf(pool.minRolls));
		pool.entries.forEach(entry -> {
			System.out.println("\tweight " + String.valueOf(entry.weight));
			System.out.println("\tvalues " + String.valueOf(Arrays.toString(entry.values.toArray())));
		});
		
		List<ResourceLocation> roll = pool.roll(new Random(seed));
		System.out.println("\n\n\n\tvalues " + String.valueOf(Arrays.toString(roll.toArray())));
		
		
		return new ArrayList<>();
	}

	public PlanetData(long seed) {
		TRAITS = getTraits(seed);
	}
	
	public static void main(String[] args) {
		getTraits(0L);
	}

}
