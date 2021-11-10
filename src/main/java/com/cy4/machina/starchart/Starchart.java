package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cy4.machina.config.CommonConfig;
import com.cy4.machina.util.StringUtils;

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
		StringUtils.printlnUtf8("Planets");
		for (int i = 0; i < planets.size(); i++) {
			PlanetData p = planets.get(i);
			StringUtils.printlnUtf8(
					(i == planets.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H + p.name);
			for (int j = 0; j < p.traits.size(); j++) {
				StringUtils.printlnUtf8((i == planets.size() - 1 ? " " : StringUtils.TREE_V) + " "
						+ (j == p.traits.size() - 1 ? StringUtils.TREE_L : StringUtils.TREE_F) + StringUtils.TREE_H
						+ p.traits.get(j).toString());
			}
		}
	}
}
