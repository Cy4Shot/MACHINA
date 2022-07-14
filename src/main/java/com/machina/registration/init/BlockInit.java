package com.machina.registration.init;

import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.BatteryBlock;
import com.machina.block.CableBlock;
import com.machina.block.CargoCrateBlock;
import com.machina.block.ComponentAnalyzerBlock;
import com.machina.block.CreativeBatteryBlock;
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

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	public static final RegistryObject<ShipConsoleBlock> SHIP_CONSOLE = register("ship_console", ShipConsoleBlock::new);
	public static final RegistryObject<CargoCrateBlock> CARGO_CRATE = register("cargo_crate", CargoCrateBlock::new);
	public static final RegistryObject<ComponentAnalyzerBlock> COMPONENT_ANALYZER = register("component_analyzer",
			ComponentAnalyzerBlock::new);
	public static final RegistryObject<BatteryBlock> BATTERY = register("battery", BatteryBlock::new);
	public static final RegistryObject<CreativeBatteryBlock> CREATIVE_BATTERY = register("creative_battery",
			CreativeBatteryBlock::new);
	public static final RegistryObject<PuzzleBlock> PUZZLE_BLOCK = register("puzzle_block", PuzzleBlock::new);
	public static final RegistryObject<CableBlock> CABLE = register("cable", CableBlock::new);
	public static final RegistryObject<Block> IRON_CHASSIS = register("iron_chassis", Blocks.IRON_BLOCK,
			a -> a.noOcclusion(), Block::new);
	public static final RegistryObject<Block> STEEL_CHASSIS = register("steel_chassis", Blocks.IRON_BLOCK,
			a -> a.noOcclusion(), Block::new);
	public static final RegistryObject<Block> STEEL_BLOCK = register("steel_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_BLOCK = register("aluminum_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_ORE = register("aluminum_ore", Blocks.IRON_ORE);
	public static final RegistryObject<Block> ALIEN_STONE = register("alien_stone", Blocks.STONE);
	public static final RegistryObject<StairsBlock> ALIEN_STONE_STAIRS = stair("alien_stone_stairs", Blocks.STONE_SLAB);
	public static final RegistryObject<SlabBlock> ALIEN_STONE_SLAB = register("alien_stone_slab", Blocks.STONE_SLAB,
			SlabBlock::new);
	public static final RegistryObject<Block> TWILIGHT_DIRT = register("twilight_dirt", Blocks.DIRT);
	public static final RegistryObject<StairsBlock> TWILIGHT_DIRT_STAIRS = stair("twilight_dirt_stairs", Blocks.DIRT);
	public static final RegistryObject<SlabBlock> TWILIGHT_DIRT_SLAB = register("twilight_dirt_slab", Blocks.DIRT,
			SlabBlock::new);
	public static final RegistryObject<Block> WASTELAND_DIRT = register("wasteland_dirt", Blocks.DIRT);
	public static final RegistryObject<StairsBlock> WASTELAND_DIRT_STAIRS = stair("wasteland_dirt_stairs", Blocks.DIRT);
	public static final RegistryObject<SlabBlock> WASTELAND_DIRT_SLAB = register("wasteland_dirt_slab", Blocks.DIRT,
			SlabBlock::new);
	public static final RegistryObject<FallingBlock> WASTELAND_SAND = register("wasteland_sand", Blocks.SAND,
			FallingBlock::new);
	public static final RegistryObject<Block> WASTELAND_SANDSTONE = register("wasteland_sandstone", Blocks.SANDSTONE);
	public static final RegistryObject<StairsBlock> WASTELAND_SANDSTONE_STAIRS = stair("wasteland_sandstone_stairs",
			Blocks.SANDSTONE_SLAB);
	public static final RegistryObject<SlabBlock> WASTELAND_SANDSTONE_SLAB = register("wasteland_sandstone_slab",
			Blocks.SANDSTONE_SLAB, SlabBlock::new);
	public static final RegistryObject<WallBlock> WASTELAND_SANDSTONE_WALL = register("wasteland_sandstone_wall",
			Blocks.SANDSTONE_WALL, WallBlock::new);
	public static final RegistryObject<Block> REINFORCED_TILE = register("reinforced_tile", Blocks.NETHERITE_BLOCK);

	private static <T extends Block> Supplier<T> of(Block block,
			Function<AbstractBlock.Properties, AbstractBlock.Properties> extra,
			Function<AbstractBlock.Properties, T> constructor) {
		return () -> constructor.apply((extra.apply(AbstractBlock.Properties.copy(block))));
	}

	public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
		return BLOCKS.<T>register(name, block);
	}

	public static RegistryObject<Block> register(String name, Block prop) {
		return register(name, prop, a -> a, Block::new);
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<AbstractBlock.Properties, T> constructor) {
		return register(name, prop, a -> a, constructor);
	}

	public static <T extends Block> RegistryObject<T> register(String name, Block prop,
			Function<AbstractBlock.Properties, AbstractBlock.Properties> extra,
			Function<AbstractBlock.Properties, T> constructor) {
		return register(name, BlockInit.<T>of(prop, extra, constructor));
	}

	public static RegistryObject<StairsBlock> stair(String name, Block prop) {
		return register(name,
				() -> new StairsBlock(() -> prop.defaultBlockState(), AbstractBlock.Properties.copy(prop)));
	}

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry()
					.register(new BlockItem(block, new Item.Properties().tab(Registration.MACHINA_ITEM_GROUP))
							.setRegistryName(block.getRegistryName()));
		});
	}
}
