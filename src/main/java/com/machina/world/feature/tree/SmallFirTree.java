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
import com.machina.api.util.math.sdf.operator.SDFSubtraction;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class SmallFirTree implements TreeMaker {

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {
		int segments = 4 + random.nextInt(1);
		float size = MathUtil.randRange(random, 15, 25);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, segments);
		SplineUtil.offsetParts(spline, random, 0.5F, 0.3f, 0.5F);
		SDF stem = SplineUtil.buildSDF(spline, 1.0f, 0f, config.wood());

		SDF discs = null;

		float bottomrad = MathUtil.randRange(random, 2f, 3f);
		float toprad = MathUtil.randRange(random, 0f, 1f);
		float sep = size / (segments + 1);
		float height = sep;
		for (int i = 0; i < segments; i++) {
			height += sep;
			Vector3f seg = spline.get(i);
			float rad = ((float) i / (float) segments) * (toprad - bottomrad) + bottomrad;
			SDF disc = new SDFSphere(rad).setBlock(config.leaves());
			disc = new SDFScale3D(disc, 1, 1.5f / rad, 1);
			disc = new SDFDisplacement(disc, random, 5f);
			disc = new SDFTranslate(disc, seg.x, height, seg.z);
			disc = new SDFSubtraction(disc, stem);
			if (i == 0) {
				discs = disc;
			} else {
				discs = new SDFUnion(disc, discs);
			}
		}

		return new SDFUnion(discs, stem);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.leaves();
	}
}
