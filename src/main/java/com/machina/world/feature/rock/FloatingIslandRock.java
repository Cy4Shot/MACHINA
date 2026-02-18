package com.machina.world.feature.rock;

import org.joml.Vector3f;

import com.machina.api.starchart.planet_biome.PlanetBiomeSettings.PlanetBiomeBigRock;
import com.machina.api.starchart.planet_biome.RockMaker;
import com.machina.api.util.math.MathUtil;
import com.machina.api.util.math.sdf.SDF;
import com.machina.api.util.math.sdf.operator.SDFDirectionalDisplacement;
import com.machina.api.util.math.sdf.operator.SDFTranslate;
import com.machina.api.util.math.sdf.primitive.SDFCappedCone;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public class FloatingIslandRock implements RockMaker {
	@Override
	public SDF build(PlanetBiomeBigRock config, RandomSource random, WorldGenLevel l, BlockPos p) {
		int wh = l.getHeight(Types.WORLD_SURFACE_WG, p.getX(), p.getZ());
		int heightOffset = wh - p.getY();

		float height = MathUtil.randRange(random, 3, 6);
		float toprad = MathUtil.randRange(random, 2.5f, height);

		SDF rock = new SDFCappedCone(0, toprad, height).setBlock(config.block());
		rock = new SDFDirectionalDisplacement(rock, random, 3f, new Vector3f(1, 0, 1), height);
		rock = new SDFTranslate(rock, 0, heightOffset - height, 0);
		return rock;
	}

	@Override
	public boolean allowsWaterPlacement() {
		return true;
	}
}