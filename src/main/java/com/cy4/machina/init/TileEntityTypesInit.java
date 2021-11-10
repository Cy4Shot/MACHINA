package com.cy4.machina.init;

import com.cy4.machina.api.annotation.registries.RegisterTileEntityType;
import com.cy4.machina.api.annotation.registries.RegistryHolder;
import com.cy4.machina.tile_entity.RocketTile;
import com.cy4.machina.tile_entity.TankTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TileEntityType.Builder;

@RegistryHolder
public class TileEntityTypesInit {

	@RegisterTileEntityType("rocket")
    public static final TileEntityType<RocketTile> ROCKET_TILE = TileEntityType.Builder.of(RocketTile::new, BlockInit.ROCKET).build(null);
	
	@RegisterTileEntityType("tank")
	public static final TileEntityType<TankTileEntity> TANK_TILE_ENTITY_TYPE = Builder.of(TankTileEntity::new, BlockInit.TANK).build(null);
	
}
