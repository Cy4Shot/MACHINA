package com.cy4.machina.starchart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.cy4.machina.Machina;
import com.cy4.machina.api.planet.PlanetTrait;

import net.minecraft.util.ResourceLocation;

public class PlanetData {
	public List<PlanetTrait> TRAITS = new ArrayList<>();

	public static List<PlanetTrait> getTraits(long seed) {
		return Machina.TRAIT_POOL_MANAGER.getPool(new ResourceLocation(Machina.MOD_ID, "geographical_traits"))
				.roll(new Random(seed)).stream().map(rl -> PlanetTrait.registry.getValue(rl))
				.collect(Collectors.toList());
	}

	public PlanetData(long seed) {
		TRAITS = getTraits(seed);
	}
}
