package com.machina.world.feature.rock;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.sdf.SDF;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class OreDepositRock implements RockMaker {
	@Override
	public SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p) {
		l.setBlock(p.below(), config.block(), 3);
		if (random.nextFloat() < 0.5)
			l.setBlock(p, config.extra(), 3);
		return null;
	}
}