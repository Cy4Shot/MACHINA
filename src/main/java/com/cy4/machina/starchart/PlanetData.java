package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetTrait;

public class PlanetData {
	public List<PlanetTrait> TRAITS = new ArrayList<>();

	public static List<PlanetTrait> getTraits(Random rand) {
		List<PlanetTrait> res = new ArrayList<>();
		Machina.TRAIT_POOL_MANAGER.forEach((location, pool) -> res.addAll(
				pool.roll(rand).stream().map(rl -> PlanetTrait.registry.getValue(rl)).collect(Collectors.toList())));
		return res;
	}

	public PlanetData(Random rand) {
		TRAITS = getTraits(rand);
	}
}
