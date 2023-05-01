package com.machina.registration.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.tile.base.CableTileEntity;
import com.machina.block.tile.base.CustomModelTileEntity;
import com.machina.block.tile.base.PuzzleTileEntity;
import com.machina.block.tile.base.TintedTileEntity;
import com.machina.block.tile.machine.AtmosphericSeparatorTileEntity;
import com.machina.block.tile.machine.BatteryTileEntity;
import com.machina.block.tile.machine.BlueprinterTileEntity;
import com.machina.block.tile.machine.CargoCrateTileEntity;
import com.machina.block.tile.machine.ComponentAnalyzerTileEntity;
import com.machina.block.tile.machine.CreativeBatteryTileEntity;
import com.machina.block.tile.machine.FabricatorTileEntity;
import com.machina.block.tile.machine.FluidHopperTileEntity;
import com.machina.block.tile.machine.FuelStorageUnitTileEntity;
import com.machina.block.tile.machine.FurnaceGeneratorTileEntity;
import com.machina.block.tile.machine.PressurizedChamberTileEntity;
import com.machina.block.tile.machine.ShipConsoleTileEntity;
import com.machina.block.tile.machine.StateConverterTileEntity;
import com.machina.block.tile.machine.TankTileEntity;
import com.machina.block.tile.machine.TemperatureRegulatorTileEntity;
import com.machina.block.tile.multiblock.haber.HaberCasingTileEntity;
import com.machina.block.tile.multiblock.haber.HaberControllerTileEntity;
import com.machina.block.tile.multiblock.haber.HaberPortTileEntity;
import com.machina.block.tinted.ITinted;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {

	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Machina.MOD_ID);

	// @formatter:off
	public static final RegistryObject<TileEntityType<BlueprinterTileEntity>> BLUEPRINTER = register("blueprinter", BlueprinterTileEntity::new, () -> BlockInit.BLUEPRINTER.get());
	public static final RegistryObject<TileEntityType<FabricatorTileEntity>> FABRICATOR = register("fabricator", FabricatorTileEntity::new, () -> BlockInit.FABRICATOR.get());
	public static final RegistryObject<TileEntityType<CargoCrateTileEntity>> CARGO_CRATE = register("cargo_crate", CargoCrateTileEntity::new, () -> BlockInit.CARGO_CRATE.get());
	public static final RegistryObject<TileEntityType<ShipConsoleTileEntity>> SHIP_CONSOLE = register("ship_console", ShipConsoleTileEntity::new, () -> BlockInit.SHIP_CONSOLE.get());
	public static final RegistryObject<TileEntityType<FluidHopperTileEntity>> FLUID_HOPPER = register("fluid_hopper", FluidHopperTileEntity::new, () -> BlockInit.FLUID_HOPPER.get());
	public static final RegistryObject<TileEntityType<ComponentAnalyzerTileEntity>> COMPONENT_ANALYZER = register("component_analyzer", ComponentAnalyzerTileEntity::new, () -> BlockInit.COMPONENT_ANALYZER.get());
	public static final RegistryObject<TileEntityType<PuzzleTileEntity>> PUZZLE = register("puzzle", PuzzleTileEntity::new, () -> BlockInit.PUZZLE_BLOCK.get());
	public static final RegistryObject<TileEntityType<PressurizedChamberTileEntity>> PRESSURIZED_CHAMBER = register("pressurized_chamber", PressurizedChamberTileEntity::new, () -> BlockInit.PRESSURIZED_CHAMBER.get());
	public static final RegistryObject<TileEntityType<BatteryTileEntity>> BATTERY = register("battery", BatteryTileEntity::new, () -> BlockInit.BATTERY.get());
	public static final RegistryObject<TileEntityType<TankTileEntity>> TANK = register("tank", TankTileEntity::new, () -> BlockInit.TANK.get());
	public static final RegistryObject<TileEntityType<CreativeBatteryTileEntity>> CREATIVE_BATTERY = register("creative_battery", CreativeBatteryTileEntity::new, () -> BlockInit.CREATIVE_BATTERY.get());
	public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE = register("cable", CableTileEntity::new, () -> BlockInit.CABLE.get());
	public static final RegistryObject<TileEntityType<AtmosphericSeparatorTileEntity>> ATMOSPHERIC_SEPARATOR = register("atmospheric_separator", AtmosphericSeparatorTileEntity::new, () -> BlockInit.ATMOSPHERIC_SEPARATOR.get());
	public static final RegistryObject<TileEntityType<TemperatureRegulatorTileEntity>> TEMPERATURE_REGULATOR = register("temperature_regulator", TemperatureRegulatorTileEntity::new, () -> BlockInit.TEMPERATURE_REGULATOR.get());
	public static final RegistryObject<TileEntityType<FuelStorageUnitTileEntity>> FUEL_STORAGE_UNIT = register("fuel_storage_unit", FuelStorageUnitTileEntity::new, () -> BlockInit.FUEL_STORAGE_UNIT.get());
	public static final RegistryObject<TileEntityType<FurnaceGeneratorTileEntity>> FURNACE_GENERATOR = register("furnace_generator", FurnaceGeneratorTileEntity::new, () -> BlockInit.FURNACE_GENERATOR.get());
	public static final RegistryObject<TileEntityType<StateConverterTileEntity>> STATE_CONVERTER = register("state_converter", StateConverterTileEntity::new, () -> BlockInit.STATE_CONVERTER.get());
	public static final RegistryObject<TileEntityType<HaberCasingTileEntity>> HABER_CASING = register("haber_casing", HaberCasingTileEntity::new, () -> BlockInit.HABER_CASING.get());
	public static final RegistryObject<TileEntityType<HaberControllerTileEntity>> HABER_CONTROLLER = register("haber_controller", HaberControllerTileEntity::new, () -> BlockInit.HABER_CONTROLLER.get());
	public static final RegistryObject<TileEntityType<HaberPortTileEntity>> HABER_PORT = register("haber_port", HaberPortTileEntity::new, () -> BlockInit.HABER_PORT.get());
	
	public static final RegistryObject<TileEntityType<TintedTileEntity>> TINTED = tinted("tinted", TintedTileEntity::new);
	public static final RegistryObject<TileEntityType<CustomModelTileEntity>> CUSTOM_MODEL = registerMany("custom_model", CustomModelTileEntity::new, () -> new Block[] { BlockInit.IRON_SCAFFOLDING.get(), BlockInit.STEEL_SCAFFOLDING.get(), BlockInit.ALUMINUM_SCAFFOLDING.get(), BlockInit.COPPER_SCAFFOLDING.get() });
	//@formatter:on

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String n, Supplier<T> s,
			Supplier<Block> b) {
		return TILES.register(n, () -> TileEntityType.Builder.of(s, b.get()).build(null));
	}

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> registerMany(String n, Supplier<T> s,
			Supplier<Block[]> b) {
		return TILES.register(n, () -> TileEntityType.Builder.of(s, b.get()).build(null));
	}

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> tinted(String n, Supplier<T> s) {
		return TILES.register(n, () -> {
			List<Block> blocks = new ArrayList<>();
			BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
				if (ITinted.class.isInstance(block)) {
					blocks.add(block);
				}
			});
			Block[] tinted = blocks.toArray(new Block[blocks.size()]);
			return TileEntityType.Builder.of(s, tinted).build(null);
		});
	}
}
