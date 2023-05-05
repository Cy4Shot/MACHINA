package com.machina.world.feature.planet.tree;

import java.util.Random;

import com.machina.util.math.MathUtil;
import com.machina.util.math.sdf.SDF;
import com.machina.util.math.sdf.operator.SDFTranslate;
import com.machina.util.math.sdf.primitive.SDFCappedCone;
import com.machina.util.math.sdf.primitive.SDFSphere;

import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

public class Leaves {
	public interface LeavesBuilder {
		boolean apply(PlanetTreeConfig config, BlockPos pos, ISeedReader region, Random rand);
	}

	public static LeavesBuilder ball(float radius, Processor proc) {
		return (config, pos, region, rand) -> {
			SDF sphere = new SDFSphere().setRadius(radius)
					.setBlock(config.getLeaves().setValue(LeavesBlock.DISTANCE, 6));

			proc.apply(sphere, rand).fillRecursive(region, pos);
			return true;
		};
	}

	public static LeavesBuilder cone(float radius, float min, float max, Processor proc) {
		return (config, pos, region, rand) -> {
			float height = MathUtil.randRange(min, max, rand);
			SDF cone = new SDFCappedCone().setRadius1(radius).setRadius2(0).setHeight(height)
					.setBlock(config.getLeaves().setValue(LeavesBlock.DISTANCE, 6));
			cone = new SDFTranslate().setTranslate(0, 2 * height / 5, 0).setSource(cone);
			proc.apply(cone, rand).fillRecursive(region, pos);
			return true;
		};
	}
}
