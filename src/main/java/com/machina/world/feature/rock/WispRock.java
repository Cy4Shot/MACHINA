package com.machina.world.feature.rock;

import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class WispRock implements RockMaker {
	@Override
	public SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p) {
		float size = MathUtil.randRange(random, 6, 12);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineUtil.offsetParts(spline, random, 1F, 0, 1F);

		return SplineUtil.buildSDF(spline, 2.3F, 1.2F, config.block());
	}
}