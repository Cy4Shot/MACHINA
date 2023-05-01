package com.machina.world.feature.planet.tree;

import java.util.Random;

import com.machina.planet.attribute.AttributeList;
import com.machina.world.PlanetChunkGenerator;
import com.machina.world.feature.planet.PlanetBaseFeature;
import com.machina.world.feature.planet.tree.Leaves.LeavesBuilder;
import com.machina.world.feature.planet.tree.Trunks.TrunkBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

public class PlanetTreeFeature extends PlanetBaseFeature {

	public static final int TYPES = 9;

	PlanetTreeConfig conf;

	public PlanetTreeFeature(AttributeList attr) {
		super(attr);
		conf = new PlanetTreeConfig(attr);
	}

	@Override
	public boolean place(ISeedReader world, PlanetChunkGenerator chunk, Random random, BlockPos pos) {
		pos = pos.below();
		if (pos.getY() < 10)
			return false;
		BlockState below = world.getBlockState(pos.below());
		if (!below.canOcclude() || below.isAir() || !below.isSolidRender(world, pos)) {
			return false;
		}

		return place(conf.treeType, world, pos, random);
	}

	public boolean place(int id, ISeedReader world, BlockPos pos, Random random) {
		float height = conf.trunkHeight * 5;
		float height2 = conf.leavesHeight * 2.5f;
		float rad = conf.trunkRadius * 3;

		TrunkBuilder trunk;
		LeavesBuilder leaves;

		// I have hardcoded all of the trees.
		// There is no point making this configurable, no one will configure it.
		// If you are reading this, and would like to tweak a tree to make it work
		// better, create a PR.
		
		// Maybe at some point, these will load from a datapack. Maybe.

		switch (id) {
		case 11:
			// Vine A
			trunk = Trunks.vine(3, 3 + height, rad + 2f, 1.5f);
			leaves = Leaves.ball(conf.leavesRadius * 2f + 2f, Processor.of(Processor.disp(1.5f)));
			break;
		case 10:
			// Large multipine
			trunk = Trunks.umbrella(height + 12, height + height + 15, 3, rad + 1, rad);
			leaves = Leaves.cone(conf.leavesRadius * 2 + 4, height2 + 5, height2 * 2.3f + 9,
					Processor.of(Processor.noise(0.2f, 0.5f), Processor.disp(0.7f)));
			break;
		case 9:
			// Large multiball
			trunk = Trunks.umbrella(height + 12, height + height + 15, 3, rad + 1, rad);
			leaves = Leaves.ball(conf.leavesRadius * 2f + 4f, Processor.of(Processor.disp(1.5f)));
			break;
		case 8:
			// Medium multiball
			trunk = Trunks.lines(height + 12, height + height + 15);
			leaves = Leaves.ball(conf.leavesRadius * 2f + 3f, Processor.of(Processor.disp(1.5f)));
			break;
		case 7:
			// Little multiball
			trunk = Trunks.lines(height + 5, height + height + 5);
			leaves = Leaves.ball(conf.leavesRadius + 2.5f, Processor.of(Processor.disp(1.5f)));
			break;
		case 6:
			// Medium multipine
			trunk = Trunks.lines(height + 12, height * 2f + 15);
			leaves = Leaves.cone(conf.leavesRadius * 2 + 2, height2 + 3, height2 + height2 + 5,
					Processor.of(Processor.noise(0.2f, 1.0f), Processor.disp(1.5f)));
			break;
		case 5:
			// Little multipine
			trunk = Trunks.lines(height + 5, height + height + 5);
			leaves = Leaves.cone(conf.leavesRadius + 2, height2 + 2, height2 + 3,
					Processor.of(Processor.noise(0.2f, 1.0f), Processor.disp(1.5f)));
			break;
		case 4:
			// Tall thin pine
			trunk = Trunks.simple(height + 15, height + height + 18, 1);
			leaves = Leaves.cone(conf.leavesRadius * 2 + 2, height2 * 2f + 10, height2 * 2f + height2 + 15,
					Processor.of(Processor.noise(0.2f, 1.7f), Processor.disp(1.5f)));
			break;
		case 3:
			// Bigger pine
			trunk = Trunks.radius(height * 1.5f + 7, height * 1.5f + height + 7, rad + 2, 1);
			leaves = Leaves.cone(conf.leavesRadius * 3 + 4, height2 * 1.5f + 7, height2 * 1.5f + height2 + 10,
					Processor.of(Processor.noise(0.2f, 1.7f), Processor.disp(2f)));
			break;
		case 2:
			// Small pine
			trunk = Trunks.simple(height + 5, height + height + 5, 1);
			leaves = Leaves.cone(conf.leavesRadius * 2 + 3, height2 + 5, height2 + height2 + 8,
					Processor.of(Processor.noise(0.2f, 1.0f), Processor.disp(1.5f)));
			break;
		case 1:
			// Baobab tree with ball leaves.
			trunk = Trunks.radius(height + 5, height + height + 5, rad + 2, 1);
			leaves = Leaves.ball(conf.leavesRadius * 2 + 3, Processor.of(Processor.disp(1.5f)));
			break;
		default:
			// Ball lollipop tree!
			trunk = Trunks.simple(height + 5, height + height + 5, 1);
			leaves = Leaves.ball(conf.leavesRadius * 2 + 3, Processor.of(Processor.disp(1.5f)));
			break;
		}

		return trunk.apply(conf, pos, world, random, leaves);
	}
}