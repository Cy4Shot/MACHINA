package com.machina.block.tile;

import com.machina.block.container.BlueprinterContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;

public class BlueprinterTileEntity extends BaseLockableTileEntity {

	public BlueprinterTileEntity(TileEntityType<?> type) {
		super(type, 2);
	}

	public BlueprinterTileEntity() {
		this(TileEntityInit.BLUEPRINTER.get());
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new BlueprinterContainer(id, player, this);
	}
}