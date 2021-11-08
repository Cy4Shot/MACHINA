package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cy4.machina.api.planet.PlanetTrait;
import com.cy4.machina.config.CommonConfig;

public class Starchart {

	public Random rand;
	public List<PlanetData> planets;

	public Starchart(long seed) {
		rand = new Random(seed);
		planets = new ArrayList<>();

		generateStarchart();
	}

	public void generateStarchart() {
		int numPlanets = rand.nextInt(CommonConfig.MAX_PLANETS.get() - CommonConfig.MIN_PLANETS.get() + 1)
				+ CommonConfig.MIN_PLANETS.get();

		for (int i = 0; i < numPlanets; i++) {
			planets.add(new PlanetData(rand));
		}
	}

	public void debugStarchart() {
		System.out.println("DEBUG STARCHART");
		for (PlanetData p : planets) {
			System.out.println("\t" + p.name);
			for (PlanetTrait t : p.traits) {
				System.out.println("\t\t" + t.getName().getString());
			}
		}
	}
}
