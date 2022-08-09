package com.machina.registration.init;

import java.util.function.Function;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.AtmosphericSeparatorBlock;
import com.machina.block.BatteryBlock;
import com.machina.block.CableBlock;
import com.machina.block.CargoCrateBlock;
import com.machina.block.ComponentAnalyzerBlock;
import com.machina.block.CreativeBatteryBlock;
import com.machina.block.FluidHopperBlock;
import com.machina.block.FuelStorageUnitBlock;
import com.machina.block.FurnaceGeneratorBlock;
import com.machina.block.PressurizedChamberBlock;
import com.machina.block.PuzzleBlock;
import com.machina.block.ShipConsoleBlock;
import com.machina.block.StateConverterBlock;
import com.machina.block.TankBlock;
import com.machina.block.TemperatureRegulatorBlock;
import com.machina.block.tinted.ITinted;
import com.machina.block.tinted.TintedBlock;
import com.machina.block.tinted.TintedFalling;
import com.machina.block.tinted.TintedSlab;
import com.machina.block.tinted.TintedStairs;
import com.machina.block.tinted.TintedWall;
import com.machina.item.TintedItem;
import com.machina.registration.Registration;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Machina.MOD_ID);

	//@formatter:off
	public static final RegistryObject<ShipConsoleBlock> SHIP_CONSOLE = register("ship_console", ShipConsoleBlock::new);
	public static final RegistryObject<FluidHopperBlock> FLUID_HOPPER = register("fluid_hopper", FluidHopperBlock::new);
	public static final RegistryObject<AtmosphericSeparatorBlock> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator", AtmosphericSeparatorBlock::new);
	public static final RegistryObject<TemperatureRegulatorBlock> TEMPERATURE_REGULATOR = register("temperature_regulator", TemperatureRegulatorBlock::new);
	public static final RegistryObject<StateConverterBlock> STATE_CONVERTER = register("state_converter", StateConverterBlock::new);
	public static final RegistryObject<FuelStorageUnitBlock> FUEL_STORAGE_UNIT = register("fuel_storage_unit", FuelStorageUnitBlock::new);
	public static final RegistryObject<FurnaceGeneratorBlock> FURNACE_GENERATOR = register("furnace_generator", FurnaceGeneratorBlock::new);
	public static final RegistryObject<CargoCrateBlock> CARGO_CRATE = register("cargo_crate", CargoCrateBlock::new);
	public static final RegistryObject<ComponentAnalyzerBlock> COMPONENT_ANALYZER = register("component_analyzer", ComponentAnalyzerBlock::new);
	public static final RegistryObject<PressurizedChamberBlock> PRESSURIZED_CHAMBER = register("pressurized_chamber", PressurizedChamberBlock::new);
	public static final RegistryObject<TankBlock> TANK = register("tank", TankBlock::new);
	public static final RegistryObject<BatteryBlock> BATTERY = register("battery", BatteryBlock::new);
	public static final RegistryObject<CreativeBatteryBlock> CREATIVE_BATTERY = register("creative_battery", CreativeBatteryBlock::new);
	public static final RegistryObject<PuzzleBlock> PUZZLE_BLOCK = register("puzzle_block", PuzzleBlock::new);
	public static final RegistryObject<CableBlock> CABLE = register("cable", CableBlock::new);
	public static final RegistryObject<Block> IRON_CHASSIS = register("iron_chassis", Blocks.IRON_BLOCK, a -> a.noOcclusion().isViewBlocking(BlockInit::never), Block::new);
	public static final RegistryObject<Block> STEEL_CHASSIS = register("steel_chassis", Blocks.IRON_BLOCK, a -> a.noOcclusion().isViewBlocking(BlockInit::never), Block::new);
	public static final RegistryObject<Block> STEEL_BLOCK = register("steel_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_BLOCK = register("aluminum_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> ALUMINUM_ORE = register("aluminum_ore", Blocks.IRON_ORE);
	public static final RegistryObject<Block> COPPER_BLOCK = register("copper_block", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> COPPER_ORE = register("copper_ore", Blocks.IRON_ORE);
	public static final RegistryObject<TintedBlock> ALIEN_STONE = tintedBlock("alien_stone", Blocks.STONE, 0);
	public static final RegistryObject<TintedStairs> ALIEN_STONE_STAIRS = tintedStairs("alien_stone_stairs", Blocks.STONE_SLAB, 0);
	public static final RegistryObject<TintedSlab> ALIEN_STONE_SLAB = tintedSlab("alien_stone_slab", Blocks.STONE_SLAB, 0);
	public static final RegistryObject<TintedBlock> TWILIGHT_DIRT = tintedBlock("twilight_dirt", Blocks.DIRT, 0);
	public static final RegistryObject<TintedStairs> TWILIGHT_DIRT_STAIRS = tintedStairs("twilight_dirt_stairs", Blocks.DIRT, 0);
	public static final RegistryObject<TintedSlab> TWILIGHT_DIRT_SLAB = tintedSlab("twilight_dirt_slab", Blocks.DIRT, 0);
	public static final RegistryObject<TintedBlock> WASTELAND_DIRT = tintedBlock("wasteland_dirt", Blocks.DIRT, 0);
	public static final RegistryObject<TintedStairs> WASTELAND_DIRT_STAIRS = tintedStairs("wasteland_dirt_stairs", Blocks.DIRT, 0);
	public static final RegistryObject<TintedSlab> WASTELAND_DIRT_SLAB = tintedSlab("wasteland_dirt_slab", Blocks.DIRT, 0);
	public static final RegistryObject<TintedFalling> WASTELAND_SAND = tintedFalling("wasteland_sand", Blocks.SAND, 1);
	public static final RegistryObject<TintedBlock> WASTELAND_SANDSTONE = tintedBlock("wasteland_sandstone", Blocks.SANDSTONE, 1);
	public static final RegistryObject<TintedStairs> WASTELAND_SANDSTONE_STAIRS = tintedStairs("wasteland_sandstone_stairs", Blocks.SANDSTONE_SLAB, 1);
	public static final RegistryObject<TintedSlab> WASTELAND_SANDSTONE_SLAB = tintedSlab("wasteland_sandstone_slab", Blocks.SANDSTONE_SLAB, 1);
	public static final RegistryObject<TintedWall> WASTELAND_SANDSTONE_WALL = tintedWall("wasteland_sandstone_wall", Blocks.SANDSTONE_WALL, 1);
	public static final RegistryObject<Block> REINFORCED_TILE = register("reinforced_tile", Blocks.NETHERITE_BLOCK);
	//@formatter:on

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

	public static RegistryObject<TintedBlock> tintedBlock(String name, Block prop, int tintIndex) {
		return register(name, prop, a -> a, p -> new TintedBlock(p, tintIndex));
	}

	public static RegistryObject<TintedSlab> tintedSlab(String name, Block prop, int tintIndex) {
		return register(name, prop, a -> a, p -> new TintedSlab(p, tintIndex));
	}

	public static RegistryObject<TintedWall> tintedWall(String name, Block prop, int tintIndex) {
		return register(name, prop, a -> a, p -> new TintedWall(p, tintIndex));
	}

	public static RegistryObject<TintedFalling> tintedFalling(String name, Block prop, int tintIndex) {
		return register(name, prop, a -> a, p -> new TintedFalling(p, tintIndex));
	}

	public static RegistryObject<TintedStairs> tintedStairs(String name, Block prop, int tintIndex) {
		return register(name,
				() -> new TintedStairs(() -> prop.defaultBlockState(), AbstractBlock.Properties.copy(prop), tintIndex));
	}

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		BLOCKS.getEntries().stream().filter(ro -> !FluidInit.BLOCKS.contains(ro.getId().getPath()))
				.map(RegistryObject::get).forEach(block -> {
					if (ITinted.class.isInstance(block)) {
						event.getRegistry()
								.register(new TintedItem(block, new Item.Properties().tab(Registration.PLANET_GROUP))
										.setRegistryName(block.getRegistryName()));
					} else {
						event.getRegistry()
								.register(new BlockItem(block, new Item.Properties().tab(Registration.MAIN_GROUP))
										.setRegistryName(block.getRegistryName()));
					}
				});
	}

	private static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
		return false;
	}
}
