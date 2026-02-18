package com.machina.world.feature.tree;

import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFScale3D;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class AcaciaTree implements TreeMaker {

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {

		// Create Stem
		float height = MathUtil.randRange(random, 7f, 13f);
		float xoff = MathUtil.randRangeSigned(random, 2f, 4f);
		float zoff = MathUtil.randRangeSigned(random, 2f, 4f);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, xoff, height, zoff, 6);
		SplineUtil.offsetParts(spline, random, 0.5F, 0.3f, 0.5F);
		SDF stem = SplineUtil.buildSDF(spline, 1.1f, 0.9f, config.wood());

		// Create a flattened disk of Leaves
		float rad = MathUtil.randRange(random, 4, 7);
		SDF leaves = new SDFSphere(rad).setBlock(config.leaves());
		leaves = new SDFScale3D(leaves, 1f, 1.2f / rad, 1f);
		leaves = new SDFTranslate(leaves, xoff, height + 1, zoff);
		leaves = new SDFDisplacement(leaves, random, 10f);

		// Combine the Stem and Leaves
		return new SDFUnion(stem, leaves);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.leaves();
	}

}
