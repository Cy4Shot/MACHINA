package com.cy4.machina.api.tile_entity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.World;

public interface ITickableTile extends ITickableTileEntity {
	
	@Override
	default void tick() {
		if (getWorld().isClientSide()) {
			clientTick();
		} else {
			serverTick();
		}
	}
	
	default void serverTick() {
		
	}

	default void clientTick() {
		
	}

	World getWorld();
}
