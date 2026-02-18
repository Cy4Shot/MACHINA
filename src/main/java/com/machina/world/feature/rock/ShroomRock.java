package com.machina.world.feature.rock;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.operator.SDFDisplacement;
import com.machina.api.util.math.sdf.operator.SDFScale3D;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.operator.SDFUnion;
import com.machina.api.util.math.sdf.primitive.SDFCappedCone;
import com.machina.api.util.math.sdf.primitive.SDFSphere;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class ShroomRock implements RockMaker {
	@Override
	public SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p) {

		float height = MathUtil.randRange(random, 15, 20);
		float baseratio = MathUtil.randRange(random, 0.6f, 1f);
		float topscale = MathUtil.randRange(random, 0.15f, 0.2f);
		float toprad = MathUtil.randRange(random, 10, height * 0.95f);
		float xoff = MathUtil.randRangeSigned(random, 1f, toprad * 0.15f);
		float yoff = MathUtil.randRangeSigned(random, 1f, toprad * 0.15f);

		SDF base = new SDFCappedCone(baseratio * height, 1, height * 1.5f).setBlock(config.block());
		SDF top = new SDFSphere(toprad).setBlock(config.block());
		top = new SDFScale3D(top, 1, topscale, 1);
		top = new SDFTranslate(top, xoff, height * 1.5f, yoff);
		top = new SDFDisplacement(top, random, 2f);

		SDF rock = new SDFUnion(base, top);
		rock = new SDFDisplacement(rock, random, 3f);

		return rock;
	}
}