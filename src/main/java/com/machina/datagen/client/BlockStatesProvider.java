package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
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
		simpleTintedBlock(BlockInit.WASTELAND_DIRT.get());
		simpleTintedBlock(BlockInit.TWILIGHT_DIRT.get());
		simpleTintedBlock(BlockInit.WASTELAND_SANDSTONE.get());
		simpleTintedBlock(BlockInit.WASTELAND_SAND.get());
		
		simpleTintedSlab(BlockInit.ALIEN_STONE_SLAB.get(), "alien_stone", false);
		simpleTintedSlab(BlockInit.WASTELAND_DIRT_SLAB.get(), "wasteland_dirt", false);
		simpleTintedSlab(BlockInit.TWILIGHT_DIRT_SLAB.get(), "twilight_dirt", false);
		simpleTintedSlab(BlockInit.WASTELAND_SANDSTONE_SLAB.get(), "wasteland_sandstone", true);
		

		simpleTintedStairs(BlockInit.ALIEN_STONE_STAIRS.get(), "alien_stone", false);
		simpleTintedStairs(BlockInit.WASTELAND_DIRT_STAIRS.get(), "wasteland_dirt", false);
		simpleTintedStairs(BlockInit.TWILIGHT_DIRT_STAIRS.get(), "twilight_dirt", false);
		simpleTintedStairs(BlockInit.WASTELAND_SANDSTONE_STAIRS.get(), "wasteland_sandstone", true);
		
		simpleTintedWall(BlockInit.WASTELAND_SANDSTONE_WALL.get(), "wasteland_sandstone");
		
		simpleOrientableBlock(BlockInit.BATTERY.get());
		simpleOrientableBlock(BlockInit.PUZZLE_BLOCK.get());
		
		fluid(FluidInit.LIQUID_HYDROGEN);
		fluid(FluidInit.LIQUID_AMMONIA);
		
		simpleBlock(BlockInit.STEEL_BLOCK.get());
		simpleBlock(BlockInit.ALUMINUM_BLOCK.get());
		simpleBlock(BlockInit.ALUMINUM_ORE.get());
		simpleBlock(BlockInit.TANK.get());
		simpleBlock(BlockInit.CREATIVE_BATTERY.get());
		simpleBlock(BlockInit.REINFORCED_TILE.get());
	}

	public void fluid(FluidObject obj) {
		getVariantBuilder(obj.block()).partialState().modelForState()
				.modelFile(models().cubeAll(name(obj.block()), new ResourceLocation("block/water_still"))).addModel();
	}

	public void simpleTintedBlock(Block block) {
		simpleBlock(block, tinted(block));
	}

	public void simpleOrientableBlock(Block block) {
		horizontalBlock(block, models().orientable(name(block), blockTexture(block),
				extend(blockTexture(block), "_front"), extend(blockTexture(block), "_top")));
	}

	public ModelFile tinted(Block block) {
		return models().singleTexture(name(block), blockLoc("tinted/tinted_cube_all"), "all", blockTexture(block));
	}

	public void simpleSlab(SlabBlock block, String rl, boolean sep) {
		if (sep)
			slabBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl + "_bottom"), blockLoc(rl + "_top"));
		else
			slabBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl), blockLoc(rl));
	}

	public void simpleTintedSlab(SlabBlock block, String rl, boolean sep) {
		if (sep)
			tintedSlabBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl + "_bottom"), blockLoc(rl + "_top"));
		else
			tintedSlabBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl), blockLoc(rl));
	}

	public void simpleStairs(StairsBlock block, String rl, boolean sep) {
		if (sep)
			stairsBlock(block, blockLoc(rl), blockLoc(rl + "_bottom"), blockLoc(rl + "_top"));
		else
			stairsBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl));
	}

	public void simpleTintedStairs(StairsBlock block, String rl, boolean sep) {
		if (sep)
			tintedStairsBlock(block, blockLoc(rl), blockLoc(rl + "_bottom"), blockLoc(rl + "_top"));
		else
			tintedStairsBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl));
	}

	public void simpleWall(WallBlock block, String rl) {
		wallBlock(block, blockLoc(rl));
		models().wallInventory(block.getRegistryName().toString(), blockLoc(rl));
	}

	public void simpleTintedWall(WallBlock block, String rl) {
		tintedWallBlock(block, blockLoc(rl));
		models().wallInventory(block.getRegistryName().toString(), blockLoc(rl));
	}

	private String name(Block block) {
		return block.getRegistryName().getPath();
	}

	private void tintedSlabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation side,
			ResourceLocation bottom, ResourceLocation top) {
		slabBlock(block, sideBottomTop(name(block), blockLoc("tinted/tinted_slab"), side, bottom, top),
				sideBottomTop(name(block) + "_top", blockLoc("tinted/tinted_slab_top"), side, bottom, top),
				models().getExistingFile(doubleslab));
	}

	private void tintedStairsBlock(StairsBlock block, ResourceLocation side, ResourceLocation bottom,
			ResourceLocation top) {
		stairsBlock(block, sideBottomTop(name(block), blockLoc("tinted/tinted_stairs"), side, bottom, top),
				sideBottomTop(name(block) + "_inner", blockLoc("tinted/tinted_inner_stairs"), side, bottom, top),
				sideBottomTop(name(block) + "_outer", blockLoc("tinted/tinted_outer_stairs"), side, bottom, top));
	}

	private void tintedWallBlock(WallBlock block, ResourceLocation texture) {
		wallBlock(block, wallModel(name(block) + "_post", blockLoc("tinted/tinted_wall_post"), texture),
				wallModel(name(block) + "_side", blockLoc("tinted/tinted_wall_side"), texture),
				wallModel(name(block) + "_side_tall", blockLoc("tinted/tinted_wall_side_tall"), texture));
	}

	private BlockModelBuilder sideBottomTop(String name, ResourceLocation parent, ResourceLocation side,
			ResourceLocation bottom, ResourceLocation top) {
		return models().withExistingParent(name, parent).texture("side", side).texture("bottom", bottom).texture("top",
				top);
	}

	private BlockModelBuilder wallModel(String name, ResourceLocation parent, ResourceLocation texture) {
		return models().withExistingParent(name, parent).texture("wall", texture);
	}

	private ResourceLocation blockLoc(String loc) {
		return new MachinaRL("block/" + loc);
	}

	private ResourceLocation extend(ResourceLocation rl, String suffix) {
		return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
	}

}
