package com.machina.registration.init;

import com.machina.Machina;
import com.machina.block.tile.CargoCrateTileEntity;
import com.machina.block.tile.ComponentAnalyzerTileEntity;
import com.machina.block.tile.ShipConsoleTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {

	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Machina.MOD_ID);
	
	public static final RegistryObject<TileEntityType<CargoCrateTileEntity>> CARGO_CRATE = TILES
			.register("cargo_crate", () -> TileEntityType.Builder
					.of(CargoCrateTileEntity::new, BlockInit.CARGO_CRATE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<ShipConsoleTileEntity>> SHIP_CONSOLE = TILES
			.register("ship_console", () -> TileEntityType.Builder
					.of(ShipConsoleTileEntity::new, BlockInit.SHIP_CONSOLE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<ComponentAnalyzerTileEntity>> COMPONENT_ANALYZER = TILES
			.register("component_analyzer", () -> TileEntityType.Builder
					.of(ComponentAnalyzerTileEntity::new, BlockInit.COMPONENT_ANALYZER.get()).build(null));

}
