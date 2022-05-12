package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.CargoCrateBlock;
import com.machina.block.ComponentAnalyzerBlock;
import com.machina.block.PuzzleBlock;
import com.machina.block.ShipConsoleBlock;
import com.machina.registration.Registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	public static final RegistryObject<ShipConsoleBlock> SHIP_CONSOLE = register("ship_console",
			() -> new ShipConsoleBlock());

	public static final RegistryObject<CargoCrateBlock> CARGO_CRATE = register("cargo_crate",
			() -> new CargoCrateBlock());

	public static final RegistryObject<ComponentAnalyzerBlock> COMPONENT_ANALYZER = register("component_analyzer",
			() -> new ComponentAnalyzerBlock());

	public static final RegistryObject<PuzzleBlock> PUZZLE_BLOCK = register("puzzle_block", () -> new PuzzleBlock());

	public static final RegistryObject<Block> IRON_CHASSIS = register("iron_chassis",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));

	public static final RegistryObject<Block> STEEL_CHASSIS = register("steel_chassis",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));

	public static final RegistryObject<Block> STEEL_BLOCK = register("steel_block",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));

	public static final RegistryObject<Block> ALIEN_STONE = register("alien_stone",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.STONE)));

	public static final RegistryObject<StairsBlock> ALIEN_STONE_STAIRS = register("alien_stone_stairs",
			() -> new StairsBlock(ALIEN_STONE.get().defaultBlockState(),
					AbstractBlock.Properties.copy(Blocks.STONE_STAIRS)));

	public static final RegistryObject<SlabBlock> ALIEN_STONE_SLAB = register("alien_stone_slab",
			() -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.STONE_SLAB)));

	public static final RegistryObject<Block> TWILIGHT_DIRT = register("twilight_dirt",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<StairsBlock> TWILIGHT_DIRT_STAIRS = register("twilight_dirt_stairs",
			() -> new StairsBlock(TWILIGHT_DIRT.get().defaultBlockState(), AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<SlabBlock> TWILIGHT_DIRT_SLAB = register("twilight_dirt_slab",
			() -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<Block> WASTELAND_DIRT = register("wasteland_dirt",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<StairsBlock> WASTELAND_DIRT_STAIRS = register("wasteland_dirt_stairs",
			() -> new StairsBlock(WASTELAND_DIRT.get().defaultBlockState(),
					AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<SlabBlock> WASTELAND_DIRT_SLAB = register("wasteland_dirt_slab",
			() -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.DIRT)));

	public static final RegistryObject<FallingBlock> WASTELAND_SAND = register("wasteland_sand",
			() -> new FallingBlock(AbstractBlock.Properties.copy(Blocks.SAND)));

	public static final RegistryObject<Block> WASTELAND_SANDSTONE = register("wasteland_sandstone",
			() -> new Block(AbstractBlock.Properties.copy(Blocks.SANDSTONE)));

	public static final RegistryObject<StairsBlock> WASTELAND_SANDSTONE_STAIRS = register("wasteland_sandstone_stairs",
			() -> new StairsBlock(WASTELAND_SANDSTONE.get().defaultBlockState(),
					AbstractBlock.Properties.copy(Blocks.SANDSTONE)));

	public static final RegistryObject<SlabBlock> WASTELAND_SANDSTONE_SLAB = register("wasteland_sandstone_slab",
			() -> new SlabBlock(AbstractBlock.Properties.copy(Blocks.SANDSTONE_SLAB)));

	public static final RegistryObject<WallBlock> WASTELAND_SANDSTONE_WALL = register("wasteland_sandstone_wall",
			() -> new WallBlock(AbstractBlock.Properties.copy(Blocks.SANDSTONE_WALL)));

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry()
					.register(new BlockItem(block, new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP))
							.setRegistryName(block.getRegistryName()));
		});
	}
}
