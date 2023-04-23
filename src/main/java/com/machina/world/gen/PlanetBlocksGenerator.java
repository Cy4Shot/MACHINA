package com.machina.world.gen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import com.machina.registration.init.BlockInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraftforge.fml.RegistryObject;

public class PlanetBlocksGenerator {

	//@formatter:off
	@SuppressWarnings("serial")
	public static Map<Integer, BasePalette> basePalette = new HashMap<Integer, BasePalette>() {{
	    put(0, new BasePalette(
	    		BlockInit.ALIEN_STONE,
	    		() -> BlockInit.ALIEN_STONE.get().defaultBlockState(),
	    		() -> BlockInit.WASTELAND_SAND.get().defaultBlockState(),
	    		() -> BlockInit.ALIEN_STONE_STAIRS.get().defaultBlockState(),
	    		() -> BlockInit.ALIEN_STONE_SLAB.get().defaultBlockState()));
	    put(1, new BasePalette(
	    		BlockInit.TWILIGHT_DIRT,
	    		() -> BlockInit.TWILIGHT_DIRT.get().defaultBlockState(),
	    		() -> BlockInit.WASTELAND_DIRT.get().defaultBlockState(),
	    		() -> BlockInit.TWILIGHT_DIRT_STAIRS.get().defaultBlockState(),
	    		() -> BlockInit.TWILIGHT_DIRT_SLAB.get().defaultBlockState()));
	    put(2, new BasePalette(
	    		BlockInit.WASTELAND_DIRT,
	    		() -> BlockInit.WASTELAND_DIRT.get().defaultBlockState(),
	    		() -> BlockInit.WASTELAND_SAND.get().defaultBlockState(),
	    		() -> BlockInit.WASTELAND_DIRT_STAIRS.get().defaultBlockState(),
	    		() -> BlockInit.WASTELAND_DIRT_SLAB.get().defaultBlockState()));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> surfacePalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		() -> BlockInit.TWILIGHT_DIRT.get().defaultBlockState(),
	    		() -> Blocks.DIRT.defaultBlockState(),
	    		() -> null,
	    		() -> null));
	    put(1, new BlockPalette(
	    		() -> BlockInit.ALIEN_STONE.get().defaultBlockState(),
	    		() -> Blocks.MAGMA_BLOCK.defaultBlockState(),
	    		() -> null,
	    		() -> null));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> fluidPalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		() -> Blocks.WATER.defaultBlockState(),
	    		() -> Blocks.ICE.defaultBlockState(),
	    		() -> null,
	    		() -> null));
	    put(1, new BlockPalette(
	    		() -> Blocks.LAVA.defaultBlockState(),
	    		() -> Blocks.MAGMA_BLOCK.defaultBlockState(),
	    		() -> null,
	    		() -> null));
	}};
	
	@SuppressWarnings("serial")
	private static Map<Integer, BlockPalette> treePalette = new HashMap<Integer, BlockPalette>() {{
	    put(0, new BlockPalette(
	    		() -> Blocks.OAK_LOG.defaultBlockState(),
	    		() -> Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true),
	    		() -> null,
	    		() -> null));
	    put(1, new BlockPalette(
	    		() -> Blocks.SPRUCE_LOG.defaultBlockState(),
	    		() -> Blocks.SPRUCE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true),
	    		() -> null,
	    		() -> null));
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

	public static BasePalette getBasePalette(int id) {
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

		private final Supplier<BlockState> baseBlock, secondaryBlock, stairBlock, slabBlock;

		public BlockPalette(Supplier<BlockState> baseBlock, Supplier<BlockState> secondaryBlock,
				Supplier<BlockState> stairBlock, Supplier<BlockState> slabBlock) {
			this.baseBlock = baseBlock;
			this.secondaryBlock = secondaryBlock;
			this.stairBlock = stairBlock;
			this.slabBlock = slabBlock;
		}

		public BlockState getBaseBlock() {
			return baseBlock.get();
		}

		public BlockState getSecondaryBlock() {
			return secondaryBlock.get();
		}

		public BlockState getStairBlock() {
			return stairBlock.get();
		}

		public BlockState getSlabBlock() {
			return slabBlock.get();
		}
	}

	public static class BasePalette extends BlockPalette {

		private final RegistryObject<? extends Block> baseReg;

		public BasePalette(RegistryObject<? extends Block> baseReg, Supplier<BlockState> baseBlock,
				Supplier<BlockState> secondaryBlock, Supplier<BlockState> stairBlock, Supplier<BlockState> slabBlock) {
			super(baseBlock, secondaryBlock, stairBlock, slabBlock);

			this.baseReg = baseReg;
		}

		public RegistryObject<? extends Block> getBaseReg() {
			return baseReg;
		}
	}

	@SuppressWarnings("unchecked")
	public static RegistryObject<? extends Block>[] getAllBases() {
		return basePalette.values().stream().map(base -> base.getBaseReg()).toArray(RegistryObject[]::new);
	}
}
