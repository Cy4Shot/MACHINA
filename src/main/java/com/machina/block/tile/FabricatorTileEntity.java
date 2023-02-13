package com.machina.block.tile;

import com.machina.block.container.FabricatorContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;

public class FabricatorTileEntity extends BaseLockableTileEntity {

	public FabricatorTileEntity(TileEntityType<?> type) {
		super(type, 17);
	}

	public FabricatorTileEntity() {
		this(TileEntityInit.FABRICATOR.get());
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new FabricatorContainer(id, player, this);
	}
}