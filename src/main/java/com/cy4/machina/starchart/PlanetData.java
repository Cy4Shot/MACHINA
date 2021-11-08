package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetNameGenerator;
import com.cy4.machina.api.planet.PlanetTrait;

public class PlanetData {
	public List<PlanetTrait> traits = new ArrayList<>();
	public String name;

	public static List<PlanetTrait> getTraits(Random rand) {
		List<PlanetTrait> res = new ArrayList<>();
		Machina.traitPoolManager.forEach((location, pool) -> res.addAll(
				pool.roll(rand).stream().map(rl -> PlanetTrait.REGISTRY.getValue(rl)).collect(Collectors.toList())));
		return res;
	}
	
	private final Random rand;

	public PlanetData(Random rand) {
		this.rand = rand;
		this.name = PlanetNameGenerator.getName(rand);
		traits = getTraits(rand);
	}
	
	@Override
	public int hashCode() {
		return rand.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PlanetData ? rand.equals(((PlanetData) obj).rand) : false;
	}
}
