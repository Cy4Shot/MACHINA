/**
 * This code is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

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
