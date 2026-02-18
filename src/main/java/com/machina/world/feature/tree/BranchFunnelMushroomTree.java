package com.machina.world.feature.tree;

import java.util.List;
import java.util.function.Function;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeTree;
import com.machina.api.starchart.planet_biome.TreeMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.SplineUtil;
import com.machina.api.util.math.sdf.operator.SDFCoordModify;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFFlatWave;
import com.machina.api.util.math.sdf.operator.SDFScale3D;
import com.machina.api.util.math.sdf.operator.SDFSmoothUnion;
import com.machina.api.util.math.sdf.operator.SDFSubtraction;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFCappedCone;
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class BranchFunnelMushroomTree implements TreeMaker {

	@Override
	public SDF build(PlanetBiomeTree config, RandomSource random, WorldGenLevel l, BlockPos p) {

		float height = MathUtil.randRange(random, 10f, 13f);
		float xoff = MathUtil.randRangeSigned(random, 0f, 2f);
		float zoff = MathUtil.randRangeSigned(random, 0f, 2f);
		int branches = (int) MathUtil.randRange(random, 6, 13);
		Function<BlockPos, BlockState> randState = pos -> random.nextInt(10) == 0 ? config.cap() : config.stem();

		List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, xoff, height, zoff, (int) height - 2);

		SDF stem = SplineUtil.buildSDF(spline, f -> {
			float xt = f - 0.4f;
			return 3.28125f * xt * xt + 2.1f;
		}, randState);
		SDF cone = new SDFCappedCone(2.5f, 10f, 10f).setBlock(randState);
		SDF cone2 = new SDFTranslate(cone, 0, 10f, 0);
		SDF hollowcone = new SDFSubtraction(cone, cone2);
		SDF modifiedcone = new SDFCoordModify(hollowcone, vec -> {
			float shrink = 3.5f - 2f * (vec.y() / 10f);
			vec.mul(new Vector3f(shrink, 1f, shrink));
		});
		SDF scaledcone = new SDFScale3D(modifiedcone, 2.2f, 1f, 2.2f);
		SDF translatedcone = new SDFTranslate(scaledcone, xoff, height, zoff);
		SDF branchedcone = new SDFFlatWave(translatedcone, branches, 0, 1.1f);
		SDF base = new SDFSmoothUnion(stem, branchedcone, 4f);

		SDF leaves = new SDFSphere(5f).setBlock(config.cap());
		leaves = new SDFTranslate(leaves, 0, height + 9f, 0);
		leaves = new SDFDisplacement(leaves, random, 5f);

		return new SDFUnion(base, leaves);
	}

	@Override
	public BlockState getLeafAttachment(PlanetBiomeTree config, RandomSource random) {
		return config.cap();
	}
}
