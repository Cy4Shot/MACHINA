package com.machina.world.feature.tree;

import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFCappedCone;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class ConeTree implements TreeMaker {

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {
		float size = MathUtil.randRange(random, 8, 15);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineUtil.offsetParts(spline, random, 0.6F, 0.1f, 0.6F);
		SDF stem = SplineUtil.buildSDF(spline, 1.2f, 0.9f, config.wood());

		float radius = MathUtil.randRange(random, 1f, 3f);
		float width = MathUtil.randRange(random, 1f, 2f);
		float height = MathUtil.randRange(random, 2f, 6f);
		SDF leaves = new SDFCappedCone(radius, radius + width, height).setBlock(config.leaves());
		leaves = new SDFTranslate(leaves, 0, size + height / 2 - 1, 0);
		leaves = new SDFDisplacement(leaves, random, 4f);

		return new SDFUnion(stem, leaves);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.leaves();
	}

}
