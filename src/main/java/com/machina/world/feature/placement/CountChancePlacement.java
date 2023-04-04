package com.machina.world.feature.placement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.Placement;

public class CountChancePlacement extends Placement<CountChanceConfig> {

	public CountChancePlacement(Codec<CountChanceConfig> conf) {
		super(conf);
	}

	@Override
	public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, CountChanceConfig conf,
			BlockPos pos) {
		List<BlockPos> poss = new ArrayList<>();
		for (int i = 0; i < conf.count; i++) {
			if (rand.nextFloat() < 1.0F / (float) conf.chance) {
				poss.add(new BlockPos(pos.getX(),
						helper.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()), pos.getZ()));
			}
		}
		return poss.stream();
	}
}