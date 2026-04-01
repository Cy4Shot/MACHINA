package com.machina.world.feature.rock;

import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFSubtraction;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.post.SDFChanceFilter;
import com.machina.api.util.math.sdf.post.SDFSelective;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;

public class ArchRock implements RockMaker {

	@Override
	public SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p) {
		float maxdist = 16;
		float mindist = 10;
		float thickness = MathUtil.randRange(random, 1.6f, 2.0f);
		int xoff = (int) MathUtil.randRangeSigned(random, mindist, maxdist);
		int zoff = (int) MathUtil.randRangeSigned(random, mindist, maxdist);
		int yoff = l.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, p.getX() + xoff, p.getZ() + zoff) - p.getY();
		if (Math.abs(yoff) > 10)
			return null;
		float height = MathUtil.randRange(random, 10, 25);

		List<Vector3f> spline = SplineUtil.makeArch(0, 0, 0, xoff, yoff, zoff, height, 10);
		SplineUtil.offsetParts(spline, random, 0.5F, 0.5f, 0.5F);
		SDF stem = SplineUtil.buildSDF(spline, thickness, thickness, config.block());
		SDF leaves = SplineUtil.buildSDF(spline, thickness + 0.3f, thickness + 0.3f, config.extra());
		leaves = new SDFSubtraction(leaves, stem);
		SDF res = new SDFUnion(leaves, stem);
		res.addPostProcess(new SDFSelective(new SDFChanceFilter(random, 0.4f), config.extra().getBlock()));
		return res;
	}
}
