/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.init;

import com.cy4.machina.api.registry.annotation.RegisterTileEntityType;
import com.cy4.machina.api.registry.annotation.RegistryHolder;
import com.cy4.machina.tile_entity.PumpTileEntity;
import com.cy4.machina.tile_entity.RocketTile;
import com.cy4.machina.tile_entity.TankTileEntity;

import net.minecraft.tileentity.TileEntityType;

@RegistryHolder
public class TileEntityTypesInit {

	@RegisterTileEntityType("rocket_tile")
	public static final TileEntityType<RocketTile> ROCKET_TILE = TileEntityType.Builder
			.of(RocketTile::new, BlockInit.ROCKET).build(null);

	@RegisterTileEntityType("tank")
	public static final TileEntityType<TankTileEntity> TANK_TILE_ENTITY_TYPE = TileEntityType.Builder
			.of(TankTileEntity::new, BlockInit.TANK).build(null);

	@RegisterTileEntityType("pump")
	public static final TileEntityType<PumpTileEntity> PUMP_TILE_ENTITY_TYPE = TileEntityType.Builder
			.of(PumpTileEntity::new, BlockInit.PUMP).build(null);
}
