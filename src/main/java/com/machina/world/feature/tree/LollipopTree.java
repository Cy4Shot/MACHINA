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
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LollipopTree implements TreeMaker {

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {

		// Create Stem
		float height = MathUtil.randRange(random, 15, 35);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, height, 0, 6);
		SplineUtil.offsetParts(spline, random, 1.5F, 0.3f, 1.5F);
		SDF stem = SplineUtil.buildSDF(spline, 1.8f, 1.3f, config.wood());

		// Create Leaves
		float rad = MathUtil.randRange(random, 5, 10);
		SDF leaves = new SDFSphere(rad).setBlock(config.leaves());
		leaves = new SDFTranslate(leaves, 0, height, 0);
		leaves = new SDFDisplacement(leaves, random, 5f);

		// Combine Stem and Leaves
		return new SDFUnion(stem, leaves);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.leaves();
	}
}
