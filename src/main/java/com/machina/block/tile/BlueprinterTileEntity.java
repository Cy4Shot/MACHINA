package com.machina.block.tile;

import com.machina.block.container.BlueprinterContainer;
import com.machina.block.tile.base.BaseLockableTileEntity;
import com.machina.blueprint.Blueprint;
import com.machina.item.BlueprintItem;
import com.machina.registration.init.BlueprintInit;
import com.machina.registration.init.ItemInit;
import com.machina.registration.init.TileEntityInit;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
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
	
	public void etch(String id) {
		getItem(0).shrink(1);
		ItemStack stack = ItemInit.BLUEPRINT.get().getDefaultInstance();
		BlueprintItem.set(stack, BlueprintInit.BLUEPRINTS.getOrDefault(id, Blueprint.EMPTY));
		setItem(1, stack);
	}
}