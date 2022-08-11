package com.machina.world.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.machina.registration.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;

public class PlanetBlocksGenerator {

	//@formatter:off
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> basePalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		BlockInit.ALIEN_STONE.get().defaultBlockState(),
	    		BlockInit.WASTELAND_SAND.get().defaultBlockState(),
	    		BlockInit.ALIEN_STONE_STAIRS.get().defaultBlockState(),
	    		BlockInit.ALIEN_STONE_SLAB.get().defaultBlockState()));
	    put(1, new BlockPalette(
	    		BlockInit.TWILIGHT_DIRT.get().defaultBlockState(),
	    		BlockInit.WASTELAND_DIRT.get().defaultBlockState(),
	    		BlockInit.TWILIGHT_DIRT_STAIRS.get().defaultBlockState(),
	    		BlockInit.TWILIGHT_DIRT_SLAB.get().defaultBlockState()));
	    put(2, new BlockPalette(
	    		BlockInit.WASTELAND_DIRT.get().defaultBlockState(),
	    		BlockInit.WASTELAND_SAND.get().defaultBlockState(),
	    		BlockInit.WASTELAND_DIRT_STAIRS.get().defaultBlockState(),
	    		BlockInit.WASTELAND_DIRT_SLAB.get().defaultBlockState()));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> surfacePalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		BlockInit.TWILIGHT_DIRT.get().defaultBlockState(),
	    		Blocks.DIRT.defaultBlockState(),
	    		null,
	    		null));
	    put(1, new BlockPalette(
	    		BlockInit.ALIEN_STONE.get().defaultBlockState(),
	    		Blocks.MAGMA_BLOCK.defaultBlockState(),
	    		null,
	    		null));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> fluidPalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		Blocks.WATER.defaultBlockState(),
	    		Blocks.ICE.defaultBlockState(),
	    		null,
	    		null));
	    put(1, new BlockPalette(
	    		Blocks.LAVA.defaultBlockState(),
	    		Blocks.MAGMA_BLOCK.defaultBlockState(),
	    		null,
	    		null));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> treePalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		Blocks.OAK_LOG.defaultBlockState(),
	    		Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true),
	    		null,
	    		null));
	    put(1, new BlockPalette(
	    		Blocks.SPRUCE_LOG.defaultBlockState(),
	    		Blocks.SPRUCE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true),
	    		null,
	    		null));
	}};
	//@formatter:on

	public static int getRandomBase(Random rand) {
		return getRandom(basePalette, rand);
	}

	public static int getRandomSurf(Random rand) {
		return getRandom(surfacePalette, rand);
	}

	public static int getRandomFluid(Random rand) {
		return getRandom(fluidPalette, rand);
	}

	public static int getRandomTree(Random rand) {
		return getRandom(treePalette, rand);
	}

	public static BlockPalette getBasePalette(int id) {
		return basePalette.get(id);
	}

	public static BlockPalette getSurfPalette(int id) {
		return surfacePalette.get(id);
	}

	public static BlockPalette getFluidPalette(int id) {
		return fluidPalette.get(id);
	}

	public static BlockPalette getTreePalette(int id) {
		return treePalette.get(id);
	}

	private static int getRandom(Map<?, ?> p, Random rand) {
		return rand.nextInt(p.size());
	}

	public static class BlockPalette {

		private BlockState baseBlock, secondaryBlock, stairBlock, slabBlock;

		public BlockPalette(BlockState baseBlock, BlockState secondaryBlock, BlockState stairBlock,
				BlockState slabBlock) {
			this.baseBlock = baseBlock;
			this.secondaryBlock = secondaryBlock;
			this.stairBlock = stairBlock;
			this.slabBlock = slabBlock;
		}

		public BlockState getBaseBlock() {
			return baseBlock;
		}

		public BlockState getSecondaryBlock() {
			return secondaryBlock;
		}

		public BlockState getStairBlock() {
			return stairBlock;
		}

		public BlockState getSlabBlock() {
			return slabBlock;
		}
	}
}
