package com.machina.world.feature.planet.tree;

import java.util.Random;

import com.machina.planet.attribute.AttributeList;
import com.machina.registration.init.AttributeInit;
import com.machina.util.math.MathUtil;
import com.machina.world.gen.PlanetBlocksGenerator;
import com.machina.world.gen.PlanetBlocksGenerator.BlockPalette;

import net.minecraft.block.BlockState;

public class PlanetTreeConfig {

	BlockState log;
	BlockState leaves;

	public final float trunkHeight;
	public final float trunkRadius;

	public final float leavesHeight;
	public final float leavesRadius;

	public final int treeType;

	public PlanetTreeConfig(AttributeList attr) {
		BlockPalette tree = PlanetBlocksGenerator.getTreePalette(attr.getValue(AttributeInit.TREE_BLOCKS));
		this.log = tree.getBaseBlock();
		this.leaves = tree.getSecondaryBlock();

		this.trunkHeight = attr.getValue(AttributeInit.TRUNK_HEIGHT);
		this.trunkRadius = attr.getValue(AttributeInit.TRUNK_RADIUS);

		this.leavesHeight = attr.getValue(AttributeInit.LEAVES_HEIGHT);
		this.leavesRadius = attr.getValue(AttributeInit.LEAVES_RADIUS);

		Random random = new Random(attr.getValue(AttributeInit.TREE_TYPE_SEED));
		this.treeType = MathUtil.randRange(0, PlanetTreeFeature.TYPES - 1, random);
	}

	public BlockState getLog() {
		return log;
	}

	public BlockState getLeaves() {
		return leaves;
	}
}
