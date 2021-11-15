package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterTileEntityType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.tile_entities.RocketTile;

import net.minecraft.tileentity.TileEntityType;

@RegistryHolder
public class TileEntityTypesInit
{
	@RegisterTileEntityType("rocket_tile")
    public static final TileEntityType<RocketTile> ROCKET_TILE = TileEntityType.Builder.of(RocketTile::new, BlockInit.ROCKET).build(null);
	
	//@RegisterTileEntityType("pad_relay_tile")
    //public static final TileEntityType<PadRelayTile> PAD_RELAY_TILE = TileEntityType.Builder.of(PadRelayTile::new, BlockInit.PAD_SIZE_RELAY).build(null);
	
	//@RegisterTileEntityType("rocket_tile")
    //public static final TileEntityType<ConsoleTile> CONSOLE_TILE = TileEntityType.Builder.of(ConsoleTile::new, BlockInit.CONSOLE).build(null);
}
