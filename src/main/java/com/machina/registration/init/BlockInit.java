package com.machina.registration.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.machina.Machina;
import com.machina.block.AtmosphericSeparatorBlock;
import com.machina.block.BatteryBlock;
import com.machina.block.BlueprinterBlock;
import com.machina.block.CableBlock;
import com.machina.block.CargoCrateBlock;
import com.machina.block.ComponentAnalyzerBlock;
import com.machina.block.CreativeBatteryBlock;
import com.machina.block.CustomModelBlock;
import com.machina.block.FabricatorBlock;
import com.machina.block.FluidHopperBlock;
import com.machina.block.FuelStorageUnitBlock;
import com.machina.block.FurnaceGeneratorBlock;
import com.machina.block.IAnimatedBlock;
import com.machina.block.MixerBlock;
import com.machina.block.PuzzleBlock;
import com.machina.block.ShipConsoleBlock;
import com.machina.block.StateConverterBlock;
import com.machina.block.TankBlock;
import com.machina.block.TemperatureRegulatorBlock;
import com.machina.block.multiblock.haber.HaberCasingBlock;
import com.machina.block.multiblock.haber.HaberControllerBlock;
import com.machina.block.multiblock.haber.HaberPortBlock;
import com.machina.block.ore.OreBlock;
import com.machina.block.ore.OreType;
import com.machina.block.tinted.ITinted;
import com.machina.block.tinted.TintedBlock;
import com.machina.block.tinted.TintedFalling;
import com.machina.block.tinted.TintedSlab;
import com.machina.block.tinted.TintedStairs;
import com.machina.block.tinted.TintedWall;
import com.machina.client.model.CustomBlockModel;
import com.machina.item.AnimatableBlockItem;
import com.machina.item.ChemicalBlockItem;
import com.machina.item.OreBlockItem;
import com.machina.item.TintedItem;
import com.machina.registration.Registration;
import com.machina.world.gen.PlanetBlocksGenerator;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
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
	public static final RegistryObject<BlueprinterBlock> BLUEPRINTER = register("blueprinter", BlueprinterBlock::new);
	public static final RegistryObject<FabricatorBlock> FABRICATOR = register("fabricator", FabricatorBlock::new);
	public static final RegistryObject<MixerBlock> MIXER = register("mixer", MixerBlock::new);
	public static final RegistryObject<ShipConsoleBlock> SHIP_CONSOLE = register("ship_console", ShipConsoleBlock::new);
	public static final RegistryObject<FluidHopperBlock> FLUID_HOPPER = register("fluid_hopper", FluidHopperBlock::new);
	public static final RegistryObject<AtmosphericSeparatorBlock> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator", AtmosphericSeparatorBlock::new);
	public static final RegistryObject<TemperatureRegulatorBlock> TEMPERATURE_REGULATOR = register("temperature_regulator", TemperatureRegulatorBlock::new);
	public static final RegistryObject<StateConverterBlock> STATE_CONVERTER = register("state_converter", StateConverterBlock::new);
	public static final RegistryObject<FuelStorageUnitBlock> FUEL_STORAGE_UNIT = register("fuel_storage_unit", FuelStorageUnitBlock::new);
	public static final RegistryObject<FurnaceGeneratorBlock> FURNACE_GENERATOR = register("furnace_generator", FurnaceGeneratorBlock::new);
	public static final RegistryObject<CargoCrateBlock> CARGO_CRATE = register("cargo_crate", CargoCrateBlock::new);
	public static final RegistryObject<ComponentAnalyzerBlock> COMPONENT_ANALYZER = register("component_analyzer", ComponentAnalyzerBlock::new);
	public static final RegistryObject<TankBlock> TANK = register("tank", TankBlock::new);
	public static final RegistryObject<BatteryBlock> BATTERY = register("battery", BatteryBlock::new);
	public static final RegistryObject<CreativeBatteryBlock> CREATIVE_BATTERY = register("creative_battery", CreativeBatteryBlock::new);
	public static final RegistryObject<PuzzleBlock> PUZZLE_BLOCK = register("puzzle_block", PuzzleBlock::new);
	public static final RegistryObject<CableBlock> CABLE = register("cable", CableBlock::new);
	public static final RegistryObject<Block> IRON_CHASSIS = register("iron_chassis", Blocks.IRON_BLOCK);
	public static final RegistryObject<Block> STEEL_CHASSIS = register("steel_chassis", Blocks.IRON_BLOCK);
	public static final RegistryObject<CustomModelBlock> IRON_SCAFFOLDING = register("iron_scaffolding", Blocks.IRON_BLOCK, p -> new CustomModelBlock(p, "iron_scaffolding"));
	public static final RegistryObject<CustomModelBlock> STEEL_SCAFFOLDING = register("steel_scaffolding", Blocks.IRON_BLOCK, p -> new CustomModelBlock(p, "steel_scaffolding"));
	public static final RegistryObject<CustomModelBlock> ALUMINUM_SCAFFOLDING = register("aluminum_scaffolding", Blocks.IRON_BLOCK, p -> new CustomModelBlock(p, "aluminum_scaffolding"));
	public static final RegistryObject<CustomModelBlock> COPPER_SCAFFOLDING = register("copper_scaffolding", Blocks.IRON_BLOCK, p -> new CustomModelBlock(p, "copper_scaffolding"));
	public static final RegistryObject<Block> LOW_GRADE_STEEL_BLOCK = register("low_grade_steel_block", Blocks.IRON_BLOCK);
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
	public static final RegistryObject<FallingBlock> SILICA_SAND = falling("silica_sand", Blocks.SAND);
	public static final RegistryObject<HaberControllerBlock> HABER_CONTROLLER = register("haber_controller", HaberControllerBlock::new);
	public static final RegistryObject<HaberCasingBlock> HABER_CASING = register("haber_casing", HaberCasingBlock::new);
	public static final RegistryObject<HaberPortBlock> HABER_PORT = register("haber_port", HaberPortBlock::new);
	//@formatter:on

	public static final Map<OreType, Map<RegistryObject<? extends Block>, RegistryObject<Block>>> ORE_MAP = ores(
			PlanetBlocksGenerator.getAllBases());

	public static final Map<OreType, RegistryObject<Block>> ORE_BLOCKS = oreBlocks();

	@SuppressWarnings("unchecked")
	private static Map<OreType, Map<RegistryObject<? extends Block>, RegistryObject<Block>>> ores(
			RegistryObject<? extends Block>... bases) {
		Map<OreType, Map<RegistryObject<? extends Block>, RegistryObject<Block>>> ret = new HashMap<>();
		for (OreType ore : Arrays.asList(OreType.values())) {
			Map<RegistryObject<? extends Block>, RegistryObject<Block>> ores = new HashMap<>();
			for (RegistryObject<? extends Block> base : bases) {
				ores.put(base, register(ore.toString().toLowerCase() + "_" + base.getId().getPath(), Blocks.IRON_ORE,
						p -> new OreBlock(p, ore, base)));
			}
			ret.put(ore, ores);
		}
		return ret;
	};

	private static Map<OreType, RegistryObject<Block>> oreBlocks() {
		Map<OreType, RegistryObject<Block>> ret = new HashMap<>();
		for (OreType ore : Arrays.asList(OreType.values())) {
			ret.put(ore, register(ore.toString().toLowerCase() + "_block", Blocks.IRON_BLOCK));
		}
		return ret;
	};

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

	public static RegistryObject<FallingBlock> falling(String name, Block prop) {
		return register(name, prop, a -> a, FallingBlock::new);
	}

	public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
		List<Block> ores = ORE_BLOCKS.values().stream().map(RegistryObject::get).collect(Collectors.toList());
		Map<Block, OreType> types = ORE_BLOCKS.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getValue().get(), Map.Entry::getKey));
		BLOCKS.getEntries().stream().filter(ro -> !FluidInit.BLOCKS.contains(ro.getId().getPath()))
				.map(RegistryObject::get).forEach(block -> {
					if (IAnimatedBlock.class.isInstance(block)) {
						CustomBlockModel<?> model = ((IAnimatedBlock) block).getBlockModel();
						event.getRegistry()
								.register(new AnimatableBlockItem(block,
										new Item.Properties().tab(Registration.MAIN_GROUP), model)
										.setRegistryName(block.getRegistryName()));
					} else {
						if (OreBlock.class.isInstance(block)) {
							event.getRegistry().register(
									new OreBlockItem(block, new Item.Properties().tab(Registration.WORLDGEN_GROUP))
											.setRegistryName(block.getRegistryName()));
						} else {
							if (ITinted.class.isInstance(block)) {
								event.getRegistry().register(
										new TintedItem(block, new Item.Properties().tab(Registration.WORLDGEN_GROUP))
												.setRegistryName(block.getRegistryName()));
							} else {
								if (ores.contains(block)) {
									event.getRegistry().register(
											new ChemicalBlockItem(block, new Item.Properties(), types.get(block).chem())
													.setRegistryName(block.getRegistryName()));
								} else {
									event.getRegistry().register(
											new BlockItem(block, new Item.Properties().tab(Registration.MAIN_GROUP))
													.setRegistryName(block.getRegistryName()));
								}
							}
						}
					}
				});
	}

	public static boolean never(BlockState p_235436_0_, IBlockReader p_235436_1_, BlockPos p_235436_2_) {
		return false;
	}
}
