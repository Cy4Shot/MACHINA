package com.machina.datagen.client;

import com.machina.Machina;
import com.machina.block.ore.OreBlock;
import com.machina.client.model.OreModelLoader;
import com.machina.registration.init.BlockInit;
import com.machina.registration.init.FluidInit;
import com.machina.registration.init.FluidInit.FluidObject;
import com.machina.util.text.MachinaRL;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesProvider extends BlockStateProvider {

	public BlockStatesProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Machina.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		tintedBlock(BlockInit.ALIEN_STONE.get());
		tintedBlock(BlockInit.WASTELAND_DIRT.get());
		tintedBlock(BlockInit.TWILIGHT_DIRT.get());
		tintedBlock(BlockInit.WASTELAND_SANDSTONE.get());
		tintedBlock(BlockInit.WASTELAND_SAND.get());

		tintedSlab(BlockInit.ALIEN_STONE_SLAB.get(), "alien_stone", false);
		tintedSlab(BlockInit.WASTELAND_DIRT_SLAB.get(), "wasteland_dirt", false);
		tintedSlab(BlockInit.TWILIGHT_DIRT_SLAB.get(), "twilight_dirt", false);
		tintedSlab(BlockInit.WASTELAND_SANDSTONE_SLAB.get(), "wasteland_sandstone", true);

		tintedStairs(BlockInit.ALIEN_STONE_STAIRS.get(), "alien_stone", false);
		tintedStairs(BlockInit.WASTELAND_DIRT_STAIRS.get(), "wasteland_dirt", false);
		tintedStairs(BlockInit.TWILIGHT_DIRT_STAIRS.get(), "twilight_dirt", false);
		tintedStairs(BlockInit.WASTELAND_SANDSTONE_STAIRS.get(), "wasteland_sandstone", true);

		tintedWall(BlockInit.WASTELAND_SANDSTONE_WALL.get(), "wasteland_sandstone");

		simpleOrientableBlock(BlockInit.PUZZLE_BLOCK.get());

		frontOrientableBlock(BlockInit.PRESSURIZED_CHAMBER.get());
		frontOrientableBlock(BlockInit.FUEL_STORAGE_UNIT.get());
		frontOrientableBlock(BlockInit.FURNACE_GENERATOR.get());
		frontOrientableBlock(BlockInit.TEMPERATURE_REGULATOR.get());
		frontOrientableBlock(BlockInit.STATE_CONVERTER.get());
		frontOrientableBlock(BlockInit.CREATIVE_BATTERY.get());
		frontOrientableBlock(BlockInit.TANK.get());

		verticalColumnBlock(BlockInit.IRON_CHASSIS.get());
		verticalColumnBlock(BlockInit.STEEL_CHASSIS.get());

		fluid(FluidInit.LIQUID_HYDROGEN);
		fluid(FluidInit.LIQUID_AMMONIA);
		fluid(FluidInit.BRINE);
		fluid(FluidInit.SULPHUR_TRIOXIDE);
		fluid(FluidInit.HYDROCHLORIC_ACID);
		fluid(FluidInit.SULPHURIC_ACID);
		fluid(FluidInit.BROMINE);
		fluid(FluidInit.BENZENE);
		fluid(FluidInit.TOULENE);
		fluid(FluidInit.METHANOL);
		fluid(FluidInit.HYDROGEN_FLUORIDE);
		fluid(FluidInit.ACETALDEHYDE);
		fluid(FluidInit.BENZYL_CHLORIDE);
		fluid(FluidInit.NITRIC_ACID);
		fluid(FluidInit.BROMOBENZENE);
		fluid(FluidInit.GLYOXAL);
		fluid(FluidInit.BENZYLAMINE);
		fluid(FluidInit.HNIW);
		fluid(FluidInit.HEXOGEN);

		simpleBlock(BlockInit.LOW_GRADE_STEEL_BLOCK.get());
		simpleBlock(BlockInit.ALUMINUM_BLOCK.get());
		simpleBlock(BlockInit.ALUMINUM_ORE.get());
		simpleBlock(BlockInit.COPPER_BLOCK.get());
		simpleBlock(BlockInit.COPPER_ORE.get());
		simpleBlock(BlockInit.REINFORCED_TILE.get());
		simpleBlock(BlockInit.HABER_CASING.get());
		simpleBlock(BlockInit.HABER_PORT.get());
		
		machine(BlockInit.HABER_CONTROLLER.get(), BlockInit.HABER_CASING.get());

		orientableGeo(BlockInit.COMPONENT_ANALYZER.get(), BlockInit.ALUMINUM_BLOCK.get());
		geo(BlockInit.IRON_SCAFFOLDING.get(), Blocks.IRON_BLOCK);
		geo(BlockInit.STEEL_SCAFFOLDING.get(), BlockInit.LOW_GRADE_STEEL_BLOCK.get());
		geo(BlockInit.ALUMINUM_SCAFFOLDING.get(), BlockInit.ALUMINUM_BLOCK.get());
		geo(BlockInit.COPPER_SCAFFOLDING.get(), BlockInit.COPPER_BLOCK.get());
		geo(BlockInit.CARGO_CRATE.get(), BlockInit.ALUMINUM_BLOCK.get());

		BlockInit.ORE_MAP.values().forEach(m -> {
			m.values().forEach(b -> {
				OreBlock ore = (OreBlock) b.get();
				BlockModelBuilder generatorModel = models().getBuilder(ore.getRegistryName().getPath())
						.texture("particle", ore.getBgTexturePath()).texture("bg", ore.getBgTexturePath())
						.texture("fg", ore.getFgTexturePath()).customLoader((bmb, h) -> {
							return new CustomLoaderBuilder<BlockModelBuilder>(OreModelLoader.ID, bmb, h) {
							};
						}).end();
				simpleBlock(ore, generatorModel);
			});
		});
	}

	public void fluid(FluidObject obj) {
		getVariantBuilder(obj.block()).partialState().modelForState()
				.modelFile(models().cubeAll(name(obj.block()), new ResourceLocation("block/water_still"))).addModel();
	}

	public void geo(Block block, Block particle) {
		simpleBlock(block, models().singleTexture(name(block), new ResourceLocation("block/block"), "particle",
				blockTexture(particle)));
	}

	public void orientableGeo(Block block, Block particle) {
		horizontalBlock(block, models().singleTexture(name(block), new ResourceLocation("block/block"), "particle",
				blockTexture(particle)));
	}

	public void tintedBlock(Block block) {
		simpleBlock(block, tinted(block));
	}

	public void machine(Block block, Block base) {
		horizontalBlock(block, models().withExistingParent(name(block), blockLoc("machine"))
				.texture("front", blockTexture(block)).texture("sides", blockTexture(base)));
	}

	public void simpleOrientableBlock(Block block) {
		horizontalBlock(block, models().orientable(name(block), blockTexture(block),
				extend(blockTexture(block), "_front"), extend(blockTexture(block), "_top")));
	}

	public void frontOrientableBlock(Block block) {
		horizontalBlock(block,
				models().orientableWithBottom(name(block), extend(blockTexture(block), "_side"),
						extend(blockTexture(block), "_front"), extend(blockTexture(block), "_bottom"),
						extend(blockTexture(block), "_top")));

	}

	public void verticalColumnBlock(Block block) {
		simpleBlock(block, models().cubeColumn(name(block), extend(blockTexture(block), "_side"),
				extend(blockTexture(block), "_end")));

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

	public void tintedSlab(SlabBlock block, String rl, boolean sep) {
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

	public void tintedStairs(StairsBlock block, String rl, boolean sep) {
		if (sep)
			tintedStairsBlock(block, blockLoc(rl), blockLoc(rl + "_bottom"), blockLoc(rl + "_top"));
		else
			tintedStairsBlock(block, blockLoc(rl), blockLoc(rl), blockLoc(rl));
	}

	public void simpleWall(WallBlock block, String rl) {
		wallBlock(block, blockLoc(rl));
		models().wallInventory(block.getRegistryName().toString(), blockLoc(rl));
	}

	public void tintedWall(WallBlock block, String rl) {
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
