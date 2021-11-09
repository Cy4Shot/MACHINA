package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterTileEntityType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.tile_entity.RocketTile;

import net.minecraft.tileentity.TileEntityType;

@RegistryHolder
public class TileEntityTypesInit {

	@RegisterTileEntityType("rocket")
    public static final TileEntityType<RocketTile> ROCKET_TILE = TileEntityType.Builder.of(RocketTile::new, BlockInit.ROCKET).build(null);
	
}
