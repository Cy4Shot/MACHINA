package com.machina.world.feature.tree;

import java.util.List;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.VecUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFSubtraction;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class JungleTree implements TreeMaker {

	private SDF createBall(PlanetBiomeTree config, RandomSource random, Vector3f pos, int offset) {
		float rad = MathUtil.randRange(random, offset, 3 + offset);
		SDF leaves = new SDFSphere(rad).setBlock(config.leaves());
		leaves = new SDFTranslate(leaves, pos.x, pos.y, pos.z);
		leaves = new SDFDisplacement(leaves, random, 10f);
		return leaves;
	}

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {
		int segments = 10 + random.nextInt(5);
		float size = MathUtil.randRange(random, 20, 50);
		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, segments);
		SplineUtil.offsetParts(spline, random, 1F, 0.3f, 1F);
		SDF stem = SplineUtil.buildSDF(spline, 2.0f, 0.8f, config.wood());
		SDF leaves = createBall(config, random, spline.get(segments - 1), 3);

		int offshoots = 3 + random.nextInt(4);
		int offshootSize = 8 + random.nextInt(5);
		float cosMin = (float) Math.cos(20); // Min Offshoot Angle
		float cosMax = (float) Math.cos(45); // Max Offshoot Angle

		int offset = segments / (offshoots + 1);
		for (int i = 0; i < offshoots; i++) {
			Vector3f from = spline.get(offset * (i + 1));
			float cosTheta = cosMax + random.nextFloat() * (cosMin - cosMax);
			float sinTheta = (float) Math.sqrt(1.0f - cosTheta * cosTheta);
			float phi = (float) (2.0 * Math.PI * random.nextFloat());
			Vector3f tangent = new Vector3f(1, 0, 0);
			if (Math.abs(VecUtil.YP.dot(tangent)) > 0.999f) {
				tangent.set(0, 0, 1);
			}
			tangent.cross(VecUtil.YP).normalize();
			Vector3f bitangent = new Vector3f(VecUtil.YP).cross(tangent);
			Vector3f direction = new Vector3f(VecUtil.YP).mul(cosTheta)
					.add(new Vector3f(tangent).mul((float) Math.cos(phi) * sinTheta))
					.add(new Vector3f(bitangent).mul((float) Math.sin(phi) * sinTheta)).normalize();
			float distance = random.nextFloat() * offshootSize;
			Vector3f to = new Vector3f(from).add(direction.mul(distance));
			List<Vector3f> offshoot = SplineUtil.makeSpline(from.x, from.y, from.z, to.x, to.y, to.z, 5);
			SplineUtil.offsetParts(offshoot, random, 1F, 0.3f, 1F);
			SDF stem2 = SplineUtil.buildSDF(offshoot, 2.0f, 0.8f, config.wood());
			stem = new SDFUnion(stem, stem2);
			leaves = new SDFUnion(leaves, createBall(config, random, to, 1));
		}

		leaves = new SDFSubtraction(leaves, stem);
		return new SDFUnion(stem, leaves);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.leaves();
	}

}
