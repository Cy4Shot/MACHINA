package com.machina.registration.init;

import java.util.function.Supplier;

import com.machina.Machina;
import com.machina.block.tile.BatteryTileEntity;
import com.machina.block.tile.CableTileEntity;
import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.block.tile.CreativeBatteryTileEntity;
import com.machina.block.tile.PuzzleTileEntity;
import com.machina.block.tile.ShipConsoleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {

	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Machina.MOD_ID);

	public static final RegistryObject<TileEntityType<CargoCrateTileEntity>> CARGO_CRATE = register("cargo_crate",
			CargoCrateTileEntity::new, () -> BlockInit.CARGO_CRATE.get());

	public static final RegistryObject<TileEntityType<ShipConsoleTileEntity>> SHIP_CONSOLE = register("ship_console",
			ShipConsoleTileEntity::new, () -> BlockInit.SHIP_CONSOLE.get());

	public static final RegistryObject<TileEntityType<ComponentAnalyzerTileEntity>> COMPONENT_ANALYZER = register(
			"component_analyzer", ComponentAnalyzerTileEntity::new, () -> BlockInit.COMPONENT_ANALYZER.get());

	public static final RegistryObject<TileEntityType<PuzzleTileEntity>> PUZZLE = register("puzzle",
			PuzzleTileEntity::new, () -> BlockInit.PUZZLE_BLOCK.get());

	public static final RegistryObject<TileEntityType<BatteryTileEntity>> BATTERY = register("battery",
			BatteryTileEntity::new, () -> BlockInit.BATTERY.get());

	public static final RegistryObject<TileEntityType<CreativeBatteryTileEntity>> CREATIVE_BATTERY = register(
			"creative_battery", CreativeBatteryTileEntity::new, () -> BlockInit.CREATIVE_BATTERY.get());

	public static final RegistryObject<TileEntityType<CableTileEntity>> CABLE = register("cable", CableTileEntity::new,
			() -> BlockInit.CABLE.get());

	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String n, Supplier<T> s,
			Supplier<Block> b) {
		return TILES.register(n, () -> TileEntityType.Builder.of(s, b.get()).build(null));
	}

}
