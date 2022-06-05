package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.tile.BatteryTileEntity;
import com.machina.block.tile.CableTileEntity;
import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.block.tile.CreativeBatteryTileEntity;
import com.machina.block.tile.PuzzleTileEntity;
import com.machina.block.tile.ShipConsoleTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {

	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Machina.MOD_ID);

	public static final RegistryObject<TileEntityType<CargoCrateTileEntity>> CARGO_CRATE = TILES.register("cargo_crate",
			() -> TileEntityType.Builder.of(CargoCrateTileEntity::new, BlockInit.CARGO_CRATE.get()).build(null));

	public static final RegistryObject<TileEntityType<ShipConsoleTileEntity>> SHIP_CONSOLE = TILES.register(
			"ship_console",
			() -> TileEntityType.Builder.of(ShipConsoleTileEntity::new, BlockInit.SHIP_CONSOLE.get()).build(null));

	public static final RegistryObject<TileEntityType<ComponentAnalyzerTileEntity>> COMPONENT_ANALYZER = TILES
			.register("component_analyzer", () -> TileEntityType.Builder
					.of(ComponentAnalyzerTileEntity::new, BlockInit.COMPONENT_ANALYZER.get()).build(null));

	public static final RegistryObject<TileEntityType<PuzzleTileEntity>> PUZZLE = TILES.register("puzzle",
			() -> TileEntityType.Builder.of(PuzzleTileEntity::new, BlockInit.PUZZLE_BLOCK.get()).build(null));
	
	public static final RegistryObject<TileEntityType<BatteryTileEntity>> BATTERY = TILES.register("battery",
			() -> TileEntityType.Builder.of(BatteryTileEntity::new, BlockInit.BATTERY.get()).build(null));
	
	public static final RegistryObject<TileEntityType<CreativeBatteryTileEntity>> CREATIVE_BATTERY = TILES.register("creative_battery",
			() -> TileEntityType.Builder.of(CreativeBatteryTileEntity::new, BlockInit.CREATIVE_BATTERY.get()).build(null));
	
	public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE = TILES.register("cable",
			() -> TileEntityType.Builder.of(CableTileEntity::new, BlockInit.CABLE.get()).build(null));

}
