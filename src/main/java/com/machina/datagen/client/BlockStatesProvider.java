package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.util.MachinaRL;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesProvider extends BlockStateProvider {

	public BlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Machina.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleTintedBlock(BlockInit.ALIEN_STONE.get());
		simpleTintedSlab(BlockInit.ALIEN_STONE_SLAB.get(), "alien_stone", false);
		simpleTintedStairs(BlockInit.ALIEN_STONE_STAIRS.get(), "alien_stone", false);
		simpleTintedBlock(BlockInit.TWILIGHT_DIRT.get());
		simpleTintedSlab(BlockInit.TWILIGHT_DIRT_SLAB.get(), "twilight_dirt", false);
		simpleTintedStairs(BlockInit.TWILIGHT_DIRT_STAIRS.get(), "twilight_dirt", false);
		simpleTintedBlock(BlockInit.WASTELAND_DIRT.get());
		simpleTintedSlab(BlockInit.WASTELAND_DIRT_SLAB.get(), "wasteland_dirt", false);
		simpleTintedStairs(BlockInit.WASTELAND_DIRT_STAIRS.get(), "wasteland_dirt", false);
		simpleTintedBlock(BlockInit.WASTELAND_SAND.get());
		simpleTintedBlock(BlockInit.WASTELAND_SANDSTONE.get());
		simpleTintedSlab(BlockInit.WASTELAND_SANDSTONE_SLAB.get(), "wasteland_sandstone", true);
		simpleTintedStairs(BlockInit.WASTELAND_SANDSTONE_STAIRS.get(), "wasteland_sandstone", true);
		simpleTintedWall(BlockInit.WASTELAND_SANDSTONE_WALL.get(), "wasteland_sandstone");
		simpleBlock(BlockInit.CARGO_CRATE.get());
		simpleBlock(BlockInit.STEEL_BLOCK.get());
		simpleBlock(BlockInit.STEEL_CHASSIS.get());
		simpleBlock(BlockInit.IRON_CHASSIS.get());
		simpleBlock(BlockInit.PUZZLE_BLOCK.get());
	}

	public void fluid(Block block) {
		getVariantBuilder(block).partialState().modelForState()
				.modelFile(models().cubeAll(name(block), new ResourceLocation("block/water_still"))).addModel();
	}

	public void simpleTintedBlock(Block block) {
		simpleBlock(block, tinted(block));
	}

	public ModelFile tinted(Block block) {
		return models().singleTexture(name(block), new MachinaRL("block/tinted_cube_all"), "all", blockTexture(block));
	}

	public void simpleSlab(SlabBlock block, String rl, boolean sep) {
		if (sep)
			slabBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl + "_bottom"), new MachinaRL("block/" + rl + "_top"));
		else
			slabBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl));
	}

	public void simpleTintedSlab(SlabBlock block, String rl, boolean sep) {
		if (sep)
			tintedSlabBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl + "_bottom"), new MachinaRL("block/" + rl + "_top"));
		else
			tintedSlabBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl), new MachinaRL("block/" + rl));
	}

	public void simpleStairs(StairsBlock block, String rl, boolean sep) {
		if (sep)
			stairsBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl + "_bottom"),
					new MachinaRL("block/" + rl + "_top"));
		else
			stairsBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl));
	}

	public void simpleTintedStairs(StairsBlock block, String rl, boolean sep) {
		if (sep)
			tintedStairsBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl + "_bottom"),
					new MachinaRL("block/" + rl + "_top"));
		else
			tintedStairsBlock(block, new MachinaRL("block/" + rl), new MachinaRL("block/" + rl),
					new MachinaRL("block/" + rl));
	}

	public void simpleWall(WallBlock block, String rl) {
		wallBlock(block, new MachinaRL("block/" + rl));
		models().wallInventory(block.getRegistryName().toString(), new MachinaRL("block/" + rl));
	}

	public void simpleTintedWall(WallBlock block, String rl) {
		tintedWallBlock(block, new MachinaRL("block/" + rl));
		models().wallInventory(block.getRegistryName().toString(), new MachinaRL("block/" + rl));
	}

	private String name(Block block) {
		return block.getRegistryName().getPath();
	}

	private void tintedSlabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation side,
			ResourceLocation bottom, ResourceLocation top) {
		slabBlock(block, sideBottomTop(name(block), new MachinaRL("block/tinted_slab"), side, bottom, top),
				sideBottomTop(name(block) + "_top", new MachinaRL("block/tinted_slab_top"), side, bottom, top),
				models().getExistingFile(doubleslab));
	}

	private void tintedStairsBlock(StairsBlock block, ResourceLocation side, ResourceLocation bottom,
			ResourceLocation top) {
		stairsBlock(block, sideBottomTop(name(block), new MachinaRL("block/tinted_stairs"), side, bottom, top),
				sideBottomTop(name(block) + "_inner", new MachinaRL("block/tinted_inner_stairs"), side, bottom, top),
				sideBottomTop(name(block) + "_outer", new MachinaRL("block/tinted_outer_stairs"), side, bottom, top));
	}

	private void tintedWallBlock(WallBlock block, ResourceLocation texture) {
		wallBlock(block, wallModel(name(block) + "_post", new MachinaRL("block/tinted_wall_post"), texture),
				wallModel(name(block) + "_side", new MachinaRL("block/tinted_wall_side"), texture),
				wallModel(name(block) + "_side_tall", new MachinaRL("block/tinted_wall_side_tall"), texture));
	}

	private BlockModelBuilder sideBottomTop(String name, ResourceLocation parent, ResourceLocation side,
			ResourceLocation bottom, ResourceLocation top) {
		return models().withExistingParent(name, parent).texture("side", side).texture("bottom", bottom).texture("top",
				top);
	}

	private BlockModelBuilder wallModel(String name, ResourceLocation parent, ResourceLocation texture) {
		return models().withExistingParent(name, parent).texture("wall", texture);
	}

}
