package com.machina.world.feature.planet.tree;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.machina.util.math.MathUtil;
import com.machina.util.sdf.SDF;
import com.machina.util.sdf.SplineUtil;
import com.machina.util.sdf.primitive.SDFLine;
import com.machina.world.feature.planet.tree.Leaves.LeavesBuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.ISeedReader;

public class Trunks {
	public interface TrunkBuilder {
		boolean apply(PlanetTreeConfig config, BlockPos pos, ISeedReader region, Random rand, LeavesBuilder leaves);
	}

	public static TrunkBuilder simple(float min, float max, float radius) {
		return (config, pos, region, rand, leaves) -> {
			float size = MathUtil.randRange(min, max, rand);
			SDF line = new SDFLine().setStart(0, 0, 0).setEnd(0, size, 0).setRadius(radius).setBlock(config.getLog());
			line.fillRecursive(region, pos);
			if (!leaves.apply(config, pos.offset(0, size + radius, 0), region, rand)) {
				return false;
			}
			return true;
		};
	}

	public static TrunkBuilder radius(float min, float max, float bottomRad, float topRad) {
		return (config, pos, region, rand, leaves) -> {
			float size = MathUtil.randRange(min, max, rand);
			List<Vector3f> spline = SplineUtil.makeSpline(0, 0, 0, 0, size, 0, (int) (3 + size * 3));
			SDF sdf = SplineUtil.buildSDF(spline, bottomRad, topRad, p -> config.getLog());
			sdf.fillRecursive(region, pos);
			if (!leaves.apply(config, pos.offset(0, size + topRad, 0), region, rand)) {
				return false;
			}
			return true;
		};
	}

	public static TrunkBuilder lines(float min, float max) {
		return (config, pos, region, rand, leaves) -> {
			List<Vector3f> SPLINE = Lists.newArrayList(new Vector3f(0.00F, 0.00F, 0.00F),
					new Vector3f(0.10F, 0.35F, 0.00F), new Vector3f(0.20F, 0.50F, 0.00F),
					new Vector3f(0.30F, 0.55F, 0.00F), new Vector3f(0.42F, 0.70F, 0.00F),
					new Vector3f(0.50F, 1.00F, 0.00F));

			float size = MathUtil.randRange(min, max, rand);
			int count = (int) (size * 0.3F);
			float var = (float) (MathUtil.TWO_PI / (float) (count * 3));
			float start = (float) MathUtil.randRange(0, MathUtil.TWO_PI, rand);
			for (int i = 0; i < count; i++) {
				float angle = (float) ((float) i / (float) count * MathUtil.TWO_PI + MathUtil.randRange(0, var, rand)
						+ start);
				List<Vector3f> spline = SplineUtil.copySpline(SPLINE);
				SplineUtil.rotateSpline(spline, angle);
				SplineUtil.scale(spline, size * MathUtil.randRange(0.5F, 1F, rand));
				SplineUtil.offsetParts(spline, rand, 1F, 0, 1F);
				SplineUtil.fillSpline(spline, region, config.getLog(), pos, (state) -> true);
				Vector3f last = spline.get(spline.size() - 1);
				if (!leaves.apply(config, pos.offset(last.x(), last.y(), last.z()), region, rand)) {
					return false;
				}
			}
			return true;
		};
	}
}
